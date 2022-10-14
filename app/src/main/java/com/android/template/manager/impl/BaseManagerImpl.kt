package com.android.template.manager.impl

import com.android.template.data.repository.interfaces.BaseRepository
import com.android.template.manager.interfaces.BaseManager

abstract class BaseManagerImpl(private val baseRepository: BaseRepository): BaseManager