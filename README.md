# LanguageTranslator

> Step 1. Add the JitPack repository to your build file

        allprojects {
            repositories {
              ...
              maven { url 'https://jitpack.io' }
            }
          }
    
> Step 2. Add the dependency

        dependencies {
                  implementation 'com.github.Suraj-14:LanguageTranslator:Tag'
          }
    
    
> step 3. Add to Project

        SMTranslate translateAPI = new SMTranslate(
                  Language.AUTO_DETECT,
                  Language.ODIA,
                  "Welcome to SMTranslate");
        translateAPI.setTranslateListener(new SMTranslate.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                Log.d(TAG, "onSuccess: " + translatedText);
            }
            @Override
            public void onFailure(String ErrorText) {
                Log.d(TAG, "onFailure: " + ErrorText);
            }
        });
