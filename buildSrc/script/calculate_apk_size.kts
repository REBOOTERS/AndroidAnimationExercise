fun test() {
    afterEvaluate {
        tasks.each {
            println("====task " + task)
        }
    }
}