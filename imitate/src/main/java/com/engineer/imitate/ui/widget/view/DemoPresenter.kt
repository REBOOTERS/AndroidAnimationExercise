/*
 * Copyright (C) 2018 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.engineer.imitate.ui.widget.view

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.text.Html
import androidx.annotation.DrawableRes
import com.engineer.imitate.shell.CommandResult
import com.engineer.imitate.shell.Shell
import com.jaredrummler.simplemvp.Presenter
import kotlin.coroutines.coroutineContext

@Deprecated("")
@SuppressLint("StaticFieldLeak") // cannot leak context in presenter
class DemoPresenter : Presenter<DemoView>() {

    override fun destroy() {
        super.destroy()
        Shell.SU.closeConsole()
        Shell.SH.closeConsole()
    }

    fun execute(asRoot: Boolean, vararg commands: String) {


        object : ExecuteCommandTask(asRoot) {
            @Deprecated("don't use", replaceWith = ReplaceWith("coroutines"))
            override fun onPreExecute() {
                view?.showProgress()
            }

            @Deprecated("don't use", replaceWith = ReplaceWith("coroutines"))
            override fun onPostExecute(result: CommandResult) {
                view?.hideProgress()
                view?.showResult(toHtml(result))
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, *commands)
    }

    fun checkIfRootIsAvailable() {
        object : AsyncTask<Void, Void, Boolean>() {
            @Deprecated("don't use", replaceWith = ReplaceWith("coroutines"))
            override fun doInBackground(vararg params: Void?): Boolean {
                return Shell.SU.available()
            }

            @Deprecated("don't use", replaceWith = ReplaceWith("coroutines"))
            override fun onPostExecute(available: Boolean) {
                view?.onRootAvailable(available)
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    private fun toHtml(result: CommandResult): CharSequence {
        val html = StringBuilder()

        // Exit Code:
        html.append("<p><strong>Edit Code:</strong> ")
        if (result.isSuccessful) {
            html.append("<font color='green'>").append(result.exitCode).append("</font>")
        } else {
            html.append("<font color='red'>").append(result.exitCode).append("</font>")
        }
        html.append("</p>")

        // STDOUT
        if (result.stdout.size > 0) {
            html.append("<p><strong>STDOUT:</strong></p><p>");
            var br = ""
            result.stdout.forEach { html.append(br).append(it); br = "<br>" }
            html.append("</p>")
        }

        // STDERR
        if (result.stderr.size > 0) {
            html.append("<p><strong>STDERR:</strong></p><p>");
            var br = ""
            result.stderr.forEach { html.append(br).append(it); br = "<br>" }
            html.append("</p>")
        }

        return Html.fromHtml(html.toString())
    }

    @Deprecated("don't use", replaceWith = ReplaceWith("coroutines"))
    private open inner class ExecuteCommandTask(private val asRoot: Boolean) :
        AsyncTask<String, Void, CommandResult>() {
        @Deprecated("don't use", replaceWith = ReplaceWith("coroutines"))
        override fun doInBackground(vararg commands: String): CommandResult {
            return if (asRoot) {
                Shell.SU.run(*commands)
            } else {
                Shell.SH.run(*commands)
            }
        }

    }

}