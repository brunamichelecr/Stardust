package com.bruna.stardust.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val apodFormatter = DateTimeFormatter.ofPattern("yyMMdd")

fun toApodDay(date: LocalDate): String = date.format(apodFormatter)