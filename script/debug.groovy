#!/usr/bin/env groovyclient -Cenv DEBUG -cp ../build/classes/main

import org.jggug.kobo.gastah.*

println "Begin..."

@Debug({ println("デバッグ情報とか") })
def a = "xxxxxxxxxxxxxxx"

println "Done."

