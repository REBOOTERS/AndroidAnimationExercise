package com.engineer.plugin.utils

import org.gradle.api.Project

class GitTool {
     static String getGitBranch(Project project) {
        def out = new ByteArrayOutputStream()
        project.exec {
            it.commandLine("git")
            it.args("rev-parse", "--short", "HEAD")
            it.standardOutput = out
        }
        return out.toString().trim()
    }
}