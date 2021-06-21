# Othello-GUI

##概要##

オセロのGUIアプリケーションで、ユーザの作成を行えたり、個々のユーザの戦歴(勝利数、引分数、負け)をGoogleSpreadSheetを使用し、同期することができます。

Gradleを使用しています。
コーディングにはUTF-8を使用しています。

以下の利用手順を踏まなくても利用は可能ですが個々のユーザーの戦歴の同期ができません。
Othello-GUI/src/main/java/OthelloMenu.java を実行することで利用できます。

###########

##利用手順##


下にアクセスして、１のGoogle Sheets APIを有効にしてください。
https://developers.google.com/sheets/api/quickstart/java

手順に従い、DOWNLOAD CLIENT CONFIGURATIONをクリックしてcredentials.jsonをダウンロード。

src/main 以下に resouecesディレクトリを作成し、その中にダウンロードしたcredentials.jsonを貼り付けてください。

src/main/java/OthelloMenuを実行するとブラウザが立ち上がります。
初回起動時にはアクセス許可が必要です。

まずはユーザを選択します。
このアプリは確認されていません」などと出ますが、ページ内の「詳細」の欄を開くと、credentials.json を作成した GCP 設定ページに移動できます。
アカウントを選択してアクセス許可をしてください。
以上でセットアップは完了です。


###########
