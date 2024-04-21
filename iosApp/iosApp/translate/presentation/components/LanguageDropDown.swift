//
//  LanguageDropDown.swift
//  iosApp
//
//  Created by Vladyslav Sakun on 21.04.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct LanguageDropDown: View {
    var language: UiLanguage
    var isOpen: Bool
    var selectLanguage: (UiLanguage) -> Void
    
    var body: some View {
        Menu {
            VStack {
                ForEach(
                    UiLanguage.Companion().allLanguages,
                    id: \.self.language.langCode) { language in
                        LanguageDropDownItem(
                            language: language,
                            onClick: {
                                selectLanguage(language)
                            }
                        )
                        
                    }
            }
        } label: {
            HStack {
                SmallLanguageIcon(language: language)
                Text(language.language.langName)
                    .foregroundColor(.lightBlue)
                Image(systemName: isOpen ? "chevron.up" : "c")
            }
        }
    }
}

#Preview {
    LanguageDropDown(
        language: UiLanguage(language: .german, imageName: "german"),
        isOpen:true,
        selectLanguage:{language in }
    )
}
