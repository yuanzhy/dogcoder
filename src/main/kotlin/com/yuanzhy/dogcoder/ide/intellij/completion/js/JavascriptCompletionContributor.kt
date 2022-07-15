package com.yuanzhy.dogcoder.ide.intellij.completion.js

import com.intellij.lang.javascript.completion.JSInsertHandler
import com.yuanzhy.dogcoder.ide.intellij.completion.AbstractCompletionContributor

class JavascriptCompletionContributor : AbstractCompletionContributor("javascript", JSInsertHandler.DEFAULT)