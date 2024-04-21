//
//  TextToSpeech.swift
//  iosApp
//
//  Created by Vladyslav Sakun on 21.04.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import AVFoundation

struct TextToSpeech {
    
    private let syntesizer = AVSpeechSynthesizer()
    
    func speak(text: String, language: String){
        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = AVSpeechSynthesisVoice(language: language)
        utterance.volume = 1
        syntesizer.speak(utterance)
    }
}
