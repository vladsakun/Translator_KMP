//
//  GradientSurface.swift
//  iosApp
//
//  Created by Vladyslav Sakun on 21.04.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct GradientSurface: ViewModifier {
    @Environment(\.colorScheme) var colorScheme
    
    func body(content: Content) -> some View {
        if colorScheme == .dark {
            let gradientStart = Color(hex: 0xFF23262E)
            let gradientEnd = Color(hex: 0xFF212329)
            content
                .background(
                    LinearGradient(
                        gradient: Gradient(colors: [gradientStart, gradientEnd]),
                        startPoint: .top,
                        endPoint: .bottom)
                )
        }else {
            content.background(Color.surface)
        }
    }
}

// QUESTION Is it native way?) looks more like Android
extension View {
    func gradientSurface() -> some View {
        modifier(GradientSurface())
    }
}
