class Status {
    String code
    String body

    /*

    void status(String code, Closure configurator) {
        configurator.delegate = new Status(code)
        configurator()
    }


    static Status status(String code, Closure configurator) {
        return new Status(code, configurator)
    }

     Status(String code, Closure configurator) {
        this.code = code

        def body
        configurator()
    }

     */
}
