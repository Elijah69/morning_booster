//
//  ContentView.swift
//  morningboosterIOS
//
//  Created by Ilia.Solovei on 19/01/2021.
//

import SwiftUI
import morningboosterShared

struct ContentView: View {
    let greet = Greeting().greeting()
    var body: some View {
        Text(greet)
            .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
