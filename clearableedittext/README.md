### 带清楚的editText-使用说明
```
         xml 中的使用方法
           <com.xkeshi.cashierdesk.viewlibrary.view.ClearableEditText
                   android:id="@+id/clearableEditText"
                   android:layout_width="match_parent"
                   android:layout_height="match_parent"
                   app:drawable_clear="@mipmap/ic_launcher" />  //设置清除的图标

           代码中的一些设置
       ClearableEditText et = (ClearableEditText) findViewById(R.id.clearableEditText);
             //设置回调
             et.setTextWatcherCallback(new ClearableEditText.TextWatcherCallback() {
                 @Override
                 public void handleTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
                     tv2.setText(text);
                 }
             });
```