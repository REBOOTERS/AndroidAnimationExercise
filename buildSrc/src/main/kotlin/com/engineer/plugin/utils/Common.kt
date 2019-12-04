package com.engineer.plugin.utils

import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author rookie
 * @since 11-29-2019
 */
object Common {

    @JvmStatic
    fun releaseTime(): String {
        val format = SimpleDateFormat("yyyy_MM_dd HH:mm", Locale.CHINA)
        return format.format(Date())
    }

    @JvmStatic
    fun getGitCommit(project: Project): String {
        val stdout = ByteArrayOutputStream()
        project.exec {
            it.commandLine("git")
            it.args("rev-parse", "--short", "HEAD")
            it.standardOutput = stdout
        }
        return stdout.toString().trim()
    }

    @JvmStatic
    fun getGitBranch(project: Project): String {
        val stdout = ByteArrayOutputStream()
        project.exec {
            it.commandLine("git")
            it.args("rev-parse", "--abbrev-ref", "HEAD")
            it.standardOutput = stdout
        }
        return stdout.toString().trim()
    }
}