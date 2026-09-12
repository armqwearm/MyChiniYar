# MyChiniYar release shrinking rules

# Keep Room entities/DAOs and their generated implementations discoverable.
-keep class androidx.room.** { *; }
-keep class com.chiniyar.app.** { *; }

# ML Kit uses reflection/native bindings internally; retain public API classes.
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.internal.mlkit_** { *; }

# Pinyin4j is accessed through its public API and resource data.
-keep class net.sourceforge.pinyin4j.** { *; }
-keep class io.github.zhangethan.pinyin4j.** { *; }
