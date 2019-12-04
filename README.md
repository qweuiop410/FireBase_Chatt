### Firebase를 활용한 채팅입니다.

+ 참고
  +[Firebase](https://firebase.google.com/docs/database/android/start?hl=ko)

+ 주의
   + 기본 입력은 myRef.setValue("String") 이지만 클래스 입력은 myRef.push().setValue(Class);

+ 인터넷을 사용하기위해 아래코드를 'AndroidManifest.xml'에 추가
``` xml
<uses-permission android:name="android.permission.INTERNET" />
```

+ build.gradle (Module)에 추가
```
implementation 'com.google.firebase:firebase-database:19.1.0'
```

+ UI - RecyclerView사용
+참고
  +[developers](https://developer.android.com/guide/topics/ui/layout/recyclerview)
