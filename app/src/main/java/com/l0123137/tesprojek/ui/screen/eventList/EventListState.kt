package com.l0123137.tesprojek.ui.screen.eventList

import com.l0123137.tesprojek.data.model.Event

data class EventListState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val birthdayMessage: String? = null,
    val isBirthdayCardVisible: Boolean = false
)