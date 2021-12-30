package com.alan.mvvm.base.utils

import android.graphics.Bitmap
import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal
import java.util.*

object FileUtil {
    //原始文件(不能播放)
    private const val AUDIO_PCM_BASEPATH = "/pcm/"

    //可播放的高质量音频文件
    private const val AUDIO_WAV_BASEPATH = "/wav/"


    /**
     * 获取随机的文件名
     *
     * @return
     */
    fun getRandomFileName(): String? {
        return "${System.currentTimeMillis()}"
    }


    /**
     * 重命名文件
     *
     * @param oldPath 旧文件名，绝对目录
     * @param newPath 新文件名，绝对目录
     * @return
     */
    fun rename(oldPath: String, newPath: String?): Boolean {
        //1.判断参数阈值
        if (TextUtils.isEmpty(oldPath) || TextUtils.isEmpty(newPath)) {
            return false
        }
        //2.比较是否变更了名称
        return if (oldPath.endsWith(newPath!!)) { //和原来名称一样，不需要改变
            true
        } else try {
            //3.根据新路径得到File类型数据
            val newFile = File(newPath)
            //4.判断是否已经存在同样名称的文件（即出现重名）
            if (newFile.exists()) {
                return false
            }
            //5.得到原文件File类型数据
            val file = File(oldPath)
            //6.调用固有方法去重命名
            file.renameTo(newFile)
        } catch (e: SecurityException) {
            false
        }
    }


    /**
     * @param filepath 文件全路径名称，like mnt/sda/XX.xx
     * @return 根路径，like mnt/sda
     * @Description 得到文件所在路径（即全路径去掉完整文件名）
     */
    fun getPathFromFilePath(filepath: String): String? {
        val pos = filepath.lastIndexOf('/')
        return if (pos != -1) {
            filepath.substring(0, pos)
        } else filepath
    }

    fun getNameFromFilePath(filepath: String): String? {
        val pos = filepath.lastIndexOf('/')
        return if (pos != -1) {
            filepath.substring(pos + 1)
        } else filepath
    }

    /**
     * @param path1 文件路径
     * @param path2 文件名
     * @return 新路径
     * @Description 重新整合路径，将路径一和路径二通过'/'连接起来得到新路径
     */
    fun makePath(path1: String, path2: String): String? {
        return if (path1.endsWith(File.separator)) {
            path1 + path2
        } else path1 + File.separator + path2
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file
     */
    fun deleteDir(file: File) {
        if (file.isFile) {
            file.delete()
            return
        }
        if (file.isDirectory) {
            val childFile = file.listFiles()
            if (childFile == null || childFile.size == 0) {
                file.delete()
                return
            }
            for (f in childFile) {
                deleteDir(f)
            }
            file.delete()
        }
    }

    /**
     * 对files根据名字排序
     *
     * @param files
     * @return
     */
    fun orderByName(files: Array<File?>): Array<File?>? {
        val fileList: List<File> = files.toList() as List<File>
        Arrays.sort(files)
        Collections.sort<File>(fileList, label@ Comparator { o1: File, o2: File ->
            if (o1.isDirectory && o2.isFile) return@Comparator -1
            if (o1.isFile && o2.isDirectory) return@Comparator 1
            if (o1.name.length > o2.name.length) {
                return@Comparator 1
            } else if (o1.name.length < o2.name.length) {
                return@Comparator -1
            }
            o1.name.compareTo(o2.name)
        } as Comparator<File>)
        return files
    }

    /**
     * 文件是否存在
     * @param strFile
     * @return
     */
    fun fileIsExists(strFile: String?): Boolean {
        try {
            val f = File(strFile)
            if (!f.exists()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    /**
     * 创建文件夹
     */
    fun createDir(file: String?): File {
        val appDir = File(file)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        return appDir
    }

    /**
     * 获取路径
     */
    fun getPath(parent: File?, fileName: String?): String? {
        return File(parent, fileName).absolutePath
    }

    /**
     * 获取指定文件下大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            var size2 = 0
            if (fileList != null) {
                size2 = fileList.size
                for (i in 0 until size2) {
                    // 如果下面还有文件
                    size = if (fileList[i].isDirectory) {
                        size + getFolderSize(fileList[i])
                    } else {
                        size + fileList[i].length()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 格式化单位
     * 计算缓存的大小
     *
     * @param size
     * @return
     */
    fun getFormatSize(size: Double): String? {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0 KB"
        }
        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + " KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + " MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + " GB"
        }
        val result4 = BigDecimal(teraBytes)
        return (result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + " TB")
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @return
     */
    fun saveImage(filePath: String?, fileName: String, bitmap: Bitmap): String? {
        val file = File(createDir(filePath).absolutePath + fileName)
        if (file.exists()) {
            file.delete()
        }
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }


    /**
     * pcm文件夹
     */
    fun getPcmFileAbsolutePath(fileName: String): String? {
        var fileName = fileName
        if (TextUtils.isEmpty(fileName)) {
            throw NullPointerException("fileName isEmpty")
        }
        check(isSdcardExit()) { "sd card no found" }
        var mAudioRawPath = ""
        if (isSdcardExit()) {
            if (!fileName.endsWith(".pcm")) {
                fileName = "$fileName.pcm"
            }
            val fileBasePath = StorageUtil.getExternalFileDir()?.absolutePath + AUDIO_PCM_BASEPATH
            val file = File(fileBasePath)
            //创建目录
            if (!file.exists()) {
                file.mkdirs()
            }
            mAudioRawPath = fileBasePath + fileName
        }
        return mAudioRawPath
    }

    /**
     * wav文件夹
     */
    fun getWavFileAbsolutePath(fileName: String?): String? {
        var fileName = fileName
        if (fileName == null) {
            throw NullPointerException("fileName can't be null")
        }
        check(isSdcardExit()) { "sd card no found" }
        var mAudioWavPath = ""
        if (isSdcardExit()) {
            if (!fileName.endsWith(".wav")) {
                fileName = "$fileName.wav"
            }
            val fileBasePath = StorageUtil.getExternalFileDir()?.absolutePath + AUDIO_WAV_BASEPATH
            val file = File(fileBasePath)
            //创建目录
            if (!file.exists()) {
                file.mkdirs()
            }
            mAudioWavPath = fileBasePath + fileName
        }
        return mAudioWavPath
    }

    /**
     * 判断是否有外部存储设备sdcard
     *
     * @return true | false
     */
    fun isSdcardExit(): Boolean {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) true else false
    }
}