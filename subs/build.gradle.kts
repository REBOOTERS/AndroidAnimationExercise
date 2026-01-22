// Parent project for all subs modules
// This allows running tasks on all subprojects, e.g., ./gradlew :subs:clean

tasks.register("clean", Delete::class) {
    delete(project.layout.buildDirectory)

    // Also clean all subprojects
    subprojects {
        delete(layout.buildDirectory)
    }
}
