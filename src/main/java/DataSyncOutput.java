import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

public class DataSyncOutput {

    private static final String APPLICATION_NAME = "Othello Data Server using QuickStart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    //spreadsheetId0は各自が使用するspreadsheet idに変更
    private static final String spreadsheetId0 = "1BQl_z8DSzFL2LEPp0UAK_0vVbfKmJ9oqnZmwrnv3Q_o";

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * getCredential は、tutorial からそのまま借用
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = DataSyncOutput.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private String spreadsheetId;
    private Sheets service;

    //コンストラクタ
    public DataSyncOutput(String spreadsheetId) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        this.spreadsheetId = spreadsheetId;
        this.service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /*
     * spreadsheet の指定 range の行に、複数のデータ (row) を書き込み
     */
    /*
     * spreadsheet の指定 range にデータを書き込む。
     */
    public void writeTest(String range) throws IOException {

        List<List<Object>> dataObj = new ArrayList<List<Object>>();
        File file = new File("src/main/resources/data.in");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String text;
            while ((text = br.readLine()) != null) {
                List<Object> temp = new ArrayList<Object>();
                String[] splitTextList = (String.valueOf(text)).split(",");
                for (String textSplit : splitTextList){
                    temp.add(textSplit);
                }
                //System.out.println(temp);
                dataObj.add(temp);
            }
        }
        ValueRange body = new ValueRange().setValues(dataObj);
        UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId, range, body)
                .setValueInputOption("RAW").execute();
    }

    /*
    public static void main(String... args) throws IOException, GeneralSecurityException {
        DataSyncOutput target = new DataSyncOutput(spreadsheetId0);
        target.writeTest("sheet1!A2");
    }
    */
}
