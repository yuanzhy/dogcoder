package com.yuanzhy.dogcoder.ide.intellij.settings

import com.intellij.util.messages.Topic

interface DogCoderSettingsListener {

    companion object {
        val TOPIC = Topic.create("DogcoderSettingsListener", DogCoderSettingsListener::class.java)
    }

    fun afterChanged()
}