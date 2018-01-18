# 核心程式碼

## 1.相機

實現Bridge Pattern，將流程抽象化後在子類別實作以利切換。
實現observer Pattern，將類別的溝通用interface搞定

### CameraManager
抽象函式，此類別的目的是為了方便日後Android更新時撰寫新Camera類別時流程一致方便切換

### CameraManagerOld
繼承自CameraManager，為已經棄用之舊版Camera類別實作類別。
使用舊版Camera的原因是專案建造時的API level比新版Camera的最低支援API Level來得低

### ICameraAction
此為CameraManager的Interface，供其他類別實作以利降低專案程式碼間的耦合性

## 2.文件儲存

### DocumentManager 
實現文件的儲存與讀取，文件內容的讀寫格式皆使用Byte[]
