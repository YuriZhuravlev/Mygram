package com.example.mygram.ui.message_recycler_view.view_holder

import com.example.mygram.ui.message_recycler_view.view.MessageView

interface MessageHolder {
    fun drawMessage(view: MessageView)
    fun onAttach(view: MessageView)
    fun onDetach()
}