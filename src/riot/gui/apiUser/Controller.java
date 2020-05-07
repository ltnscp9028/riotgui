package riot.gui.apiUser;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Controller{
    String inputUserNickname;
    @FXML TextField inputUser;
    @FXML Label viewUserInfo;
    @FXML Label viewUserTier;
    @FXML Label viewUserWinOrLoseSol;
    @FXML Label viewUserFreeTier;
    @FXML Pane totalRank;
    @FXML Label viewQueueType;
    @FXML Label viewSolQType;
    @FXML Label viewUserWinOrLoseFree;
    @FXML VBox vBox;
    @FXML VBox cidMastrey;
    inputUserSearch inputUserSearch = new inputUserSearch();
    @FXML
    public void searchUser(ActionEvent event) {
        inputUserNickname = inputUser.getText();
        Map<String, Object> info = inputUserSearch.returnUserInfo(inputUserNickname);
        viewUserInfo.setText((String) info.get("이름"));
        viewUserTier.setText((String) info.get("tierSol"));
        viewUserWinOrLoseSol.setText((String) info.get("winOrLoseSol"));
        viewUserFreeTier.setText((String) info.get("tierFree"));
        viewSolQType.setText((String) info.get("SolQ"));
        viewQueueType.setText((String) info.get("FreeQ"));
        viewUserWinOrLoseFree.setText((String) info.get("winOrLoseFree"));
        //        viewUserTier.setText((String) info.get("tierSol"));
//        viewUserInfo.setText((String) info.get("이름"));
        //System.out.println(inputUserSearch.MasteryChampion(inputUserNickname));
    }

    @FXML
    public void viewMostChampion(ActionEvent event) {
        cidMastrey.getChildren().clear();
        inputUserNickname = inputUser.getText();
        System.out.println(inputUserNickname);
        Map<String, Object> most = inputUserSearch.inputSearchNickname(inputUserNickname);
        System.out.println(most.get("playChampionCount")+"개");
        int t = (int) most.get("playChampionCount");
        for(int i=0;i<t;i++){
            String cMost = (String)most.get("Cid"+i)+most.get("championPlayCount"+i)+most.get("championMyKDA"+i)+most.get("championWin"+i)
                    +most.get("championLose"+i);
            Label label = new Label();
            label.setStyle("-fx-border-color:skyblue;-fx-font-family:NanumBarunGothic;-fx-font-size:15");
            label.setPrefSize(329,40);
            label.setText(cMost);
            cidMastrey.getChildren().add(label);
            ListView li = new ListView();
            li.setPrefSize(329,5);
            cidMastrey.getChildren().add(li);
        }
    }


    @FXML
    public void viewRecentMatch(ActionEvent event) {
        vBox.getChildren().clear();
        String inputUserNickname = inputUser.getText();
        Map<String, Object> recentMatch = inputUserSearch.returnMatchInfo(inputUserNickname);
        for (int i = 0; i <inputUserSearch.maxCount; i++) {
            //matchScroll.setContent((Node) recentMatch.get("queue"+i));
            String tWinOrLose = (String) recentMatch.get("winOrLose"+i);

            String rm= recentMatch.get("queue"+i)+"\n"+"                               "+recentMatch.get("kill"+i)
                    +recentMatch.get("death"+i)+recentMatch.get("assist"+i)+recentMatch.get("KDA"+i)+recentMatch.get("Cid"+i);
            if (tWinOrLose.equals("승리")) {
                Label label = new Label();
                label.setPrefSize(395,60);
                label.setText(rm);
                label.setStyle("-fx-border-color:a0bed3;-fx-background-color:a3cfec;-fx-font-family:NanumBarunGothic;");
                //listView.setItems(wdata);
                //listView.setItems((ObservableList) label);
                vBox.getChildren().add(label);
                System.out.println("승리");
            }
            else {
                ListView listView = new ListView();
                Label label = new Label();
                label.setPrefSize(395,60);
                label.setText(rm);
//                ldata.add("승리");
                //listView.setItems(ldata);
//                listView.setPrefSize(395,50);
                label.setStyle("-fx-border-color:cea7a7;-fx-background-color:e2b6b3;-fx-font-family:NanumBarunGothic;");
//                label.setStyle("-fx-border-color:cea7a7;-fx-background-color:e2b6b3;");
                //listView.setItems((ObservableList) label);
                vBox.getChildren().add(label);
                System.out.println("패배");
            }
            ListView li = new ListView();
            li.setPrefSize(400,5);
            vBox.getChildren().add(li);
        }
    }
}