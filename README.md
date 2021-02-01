# Othello-GUI
Use google sheet for Data management.

Gradleを使用しています。
コーディングにはUTF-8を使用しています。

下にアクセスして、１のGoogle Sheets APIを有効にしてください。
https://developers.google.com/sheets/api/quickstart/java

手順に従い、DOWNLOAD CLIENT CONFIGURATIONをクリックしてcredentials.jsonをダウンロード。

src/main 以下に resouecesファイルを作成し、その中にダウンロードしたcredentials.jsonを貼り付けてください。

src/main/java/OthelloMenuを実行するとブラウザが立ち上がります。
初回起動時にはアクセス許可が必要です。

まずはユーザを選択します。
このアプリは確認されていません」などと出ますが、ページ内の「詳細」の欄を開くと、credentials.json を作成した GCP 設定ページに移動できます。
アカウントを選択してアクセス許可をしてください。
以上でセットアップは完了です。
