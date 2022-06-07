
-ignorewarnings

-keep class com.google.** { *; }
-dontwarn com.google.**
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**
-keep class com.airbnb.lottie.** { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder

-keep public class com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.** {*;}
-keep public class com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.** {*;}
-keep public class com.docreader.docviewer.pdfcreator.pdfreader.filereader.CSVFileViewer.CSVModel.** {*;}
-keep public class com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.** {*;}
-keep public class com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet.** {*;}
-keep public class com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.FilesView.** {*;}
