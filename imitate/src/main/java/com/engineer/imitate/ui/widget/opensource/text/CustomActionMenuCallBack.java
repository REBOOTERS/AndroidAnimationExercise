/*
 * Copyright  2017  zengp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.engineer.imitate.ui.widget.opensource.text;


public interface CustomActionMenuCallBack {
    /**
     * 创建ActionMenu菜单。
     * 返回值false，保留默认菜单；返回值true，移除默认菜单
     *
     * @param menu
     * @return 返回false，保留默认菜单；返回true，移除默认菜单
     */
    boolean onCreateCustomActionMenu(ActionMenu menu);

    /**
     * ActionMenu菜单的点击事件
     *
     * @param itemTitle       ActionMenu菜单item的title
     * @param selectedContent 选择的文字
     */
    void onCustomActionItemClicked(String itemTitle, String selectedContent);
}
