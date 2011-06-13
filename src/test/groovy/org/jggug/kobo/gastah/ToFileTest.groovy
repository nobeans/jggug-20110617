package org.jggug.kobo.gastah

class ToFileTest extends GroovyTestCase {

    @ToFile("/tmp/hoge.tmp")
    static class TargetHolder {
    }
    @ToFile("/tmp/hoge.tmp")
    static class TargetHolder2 {
    }

    void testPrintlnx() {
        new TargetHolder().xprintln("This is test. 1")
        new TargetHolder().xprintln("This is test. 2")
        new TargetHolder().xprintln("This is test. 3")
        new TargetHolder2().xprintln("This is test. 4")
    }
}
