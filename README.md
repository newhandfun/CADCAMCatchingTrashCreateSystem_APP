# 楊老大的CADCAM小幫手

## 此APP主要功能

1.  音量鍵拍照。
2.  拍照後在規定秒數後從使用者Gamil信箱寄Email至指定信箱。
3.  信箱資訊儲存。
4.  可搭配本專題之感測器電路達成全自動化。

## 畫面預覽

1. 開啟畫面
會跳出AlertDialog詢問Email的密碼
    ![APP](https://lh5.googleusercontent.com/4ue_a5bClM_dhzVeYy8EhfLjQP0PzfRbPlsLAka5Xyv3sIa9Nw5TYICtKh0w8-I_o4x5aocwfrYr0w=w958-h954-rw "登入畫面")
    1.    信箱密碼輸入欄位。
    2.    確認按鈕。儲存密碼於此次APP執行時使用，此密碼不會儲存成檔案。

2.  執行中畫面
    ![RUNNING](https://lh6.googleusercontent.com/moU90IRPsw6bJDiLQaPzMX8ZcoppretVlaCtSBwRiTcWp5r_3kBBVU1Gluq6BDq-6Asz2PpoFov4LQ=w958-h954-rw "使用畫面")
    1.    狀態文字。顯示目前狀態，狀態分為
          1.    Waitting:預設狀態
          2.    CountDown:倒數狀態
    2.    攝影機。瀏覽目前照片。
    2.    計數器。傳送照片的倒數計時。
    2.    總時間。從拍照到傳送照片的總時間，此欄位可以於執行中及時更改。
    2.    信箱資料。從上到下分別為
          1.    使用者信箱
          2.    使用者密碼
          3.    接收人信箱
    

## 使用說明

    1.    操作單下音量鍵:拍照
        2.    連續兩下音量鍵:取消計時
        3.    如果想看全部的照片，請至"GarbageThrowerCater/Picture"中觀看
    4.    想要更改Email資訊，請至"GarbageThrowerCater/Email"中的emailData.txt檔案修改
    
## 類別圖

## 未來展望

1.    讓使用者不必開啟"允許低安全性的應用程式"選項
    ![Problem1](https://lh5.googleusercontent.com/zr2-7HywUgXLmQ38zckg_wgDcD9yV7uVhtnybzDfruGbx0HVsSmmCppm57_JgugTFd6kNiUciT6J0Q=w958-h954-rw "低安全性")
2.    Email資料儲存加密
3.    讓使用者可以更改信件標題、內文等等

## 已知BUG
1.    切換APP時可能會閃退
