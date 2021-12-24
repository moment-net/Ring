package com.alan.module.chat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 作者：alan
 * 时间：2021/8/1
 * 备注：
 */
class ViewpagerAdapter(
    var mDatas: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = mDatas.size

    override fun createFragment(position: Int): Fragment = mDatas[position]

}