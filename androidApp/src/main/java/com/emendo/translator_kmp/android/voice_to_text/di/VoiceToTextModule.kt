package com.emendo.translator_kmp.android.voice_to_text.di

import android.app.Application
import com.emendo.translator_kmp.android.voice_to_text.data.AndroidVoiceToTextParser
import com.emendo.translator_kmp.voice_to_text.domain.VoiceToTextParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object VoiceToTextModule {

  @Provides
  @ViewModelScoped
  fun provideVoiceToTextParser(app: Application): VoiceToTextParser {
    return AndroidVoiceToTextParser(app)
  }
}