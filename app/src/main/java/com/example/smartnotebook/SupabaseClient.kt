package com.example.smartnotebook

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

// Substitua pelos valores do seu projeto em: supabase.com > Project Settings > API
private const val SUPABASE_URL = "https://lekmyigqoxcrdfbqfcdy.supabase.co"
private const val SUPABASE_ANON_KEY = "sb_publishable_VYHSlbFSvCOgLDFam08OZQ_yMCpX1V7default"

val supabase = createSupabaseClient(
    supabaseUrl = SUPABASE_URL,
    supabaseKey = SUPABASE_ANON_KEY
) {
    install(Auth)
    install(Postgrest)
    install(Realtime)
}
