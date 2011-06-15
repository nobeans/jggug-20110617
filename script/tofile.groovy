#!/usr/bin/env groovyclient -cp ../build/classes/main

import org.jggug.kobo.gastah.*

@ToFile("/tmp/hoge.txt")
class X { }

new X().println("Hello! 1")
new X().xprintln("Hello! 1")
new X().xprintln("Hello! 2")
new X().xprintln("Hello! 3")

