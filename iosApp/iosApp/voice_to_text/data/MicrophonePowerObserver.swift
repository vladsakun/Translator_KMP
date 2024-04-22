//
//  MicrophonePowerObserver.swift
//  iosApp
//
//  Created by Vladyslav Sakun on 22.04.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared
import Speech
import Combine

class MicrophonePowerObserver: ObservableObject {
    private var cancellable: AnyCancellable? = nil
    private var audioRecorder: AVAudioRecorder? = nil
    
    @Published private(set) var micPowerRatio = 0.0
    
    private let powerRationEmissionsPerSecond = 20.0
    
    func startObserving() {
        do {
            let recorderSettings: [String: Any] = [
                AVFormatIDKey: NSNumber(value: kAudioFormatAppleLossless),
                AVNumberOfChannelsKey: 1
            ]
            
            let recorder = try AVAudioRecorder(url: URL(fileURLWithPath: "/dev/null", isDirectory: true), settings: recorderSettings)
            recorder.isMeteringEnabled = true
            recorder.record()
            self.audioRecorder = recorder
            
            self.cancellable = Timer.publish(
                every: 1.0 / powerRationEmissionsPerSecond,
                tolerance: 1.0 / powerRationEmissionsPerSecond,
                on: .main,
                in: .common
            )
            .autoconnect()
            .sink { [weak self] _ in
                recorder.updateMeters()
                
                let powerOffset = recorder.averagePower(forChannel: 0)
                if powerOffset < -50 {
                    self?.micPowerRatio = 0.0
                }else {
                    let normalizedOffset = CGFloat(50 + powerOffset) / 50
                    self?.micPowerRatio = normalizedOffset
                }
            }
        }catch {
            print("An error occured when observing microphone power: \(error.localizedDescription)")
        }
    }
    
    func release() {
        cancellable = nil
        
        audioRecorder?.stop()
        audioRecorder = nil
        
        micPowerRatio = 0.0
    }
}
