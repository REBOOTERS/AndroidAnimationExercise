package com.engineer.imitate.ui.fragments.subs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.engineer.imitate.R
import com.engineer.imitate.ui.widget.opensource.text.ActionMenu
import com.engineer.imitate.ui.widget.opensource.text.CustomActionMenuCallBack
import com.engineer.imitate.ui.widget.opensource.text.SelectableTextView

/**
 * @author Rookie
 * @since 06-30-2020
 */
class FitTextViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fit_text_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctv_content: SelectableTextView = view.findViewById(R.id.ctv_content)
        ctv_content.setCustomActionMenuCallBack(object : CustomActionMenuCallBack {
            override fun onCustomActionItemClicked(itemTitle: String?, selectedContent: String?) {
                Toast.makeText(context, "ActionMenu: " + itemTitle, Toast.LENGTH_SHORT).show()
            }

            override fun onCreateCustomActionMenu(menu: ActionMenu?): Boolean {
                val titleList: MutableList<String> = ArrayList()
                titleList.add("翻译")
                titleList.add("分享")
                titleList.add("分享")
                menu?.apply {
                    setActionMenuBgColor(-0x99999a) // ActionMenu背景色
                    setMenuItemTextColor(-0x1) // ActionMenu文字颜色
                    addCustomMenuItem(titleList) // 添加菜单
                }
                return false // 返回false，保留默认菜单(全选/复制)；返回true，移除默认菜单
            }

        })
        ctv_content.setText(R.string.content)
        ctv_content.setTextJustify(true)               // 是否启用两端对齐 默认启用
        ctv_content.setForbiddenActionMenu(false)         // 是否禁用自定义ActionMenu 默认启用
        ctv_content.setTextHighlightColor(Color.GREEN);     // 文本高亮色
    }
}