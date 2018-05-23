package com.mobiwarez.laki.sville.configuration

import java.util.UUID

class UUIDViewIdGenerator : ViewIdGenerator {

    override fun newId(): String {
        return UUID.randomUUID().toString()
    }
}
