package riot.gui.apiUser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class inputUserSearch {
    String apiKey = "?api_key=RGAPI-2d83167a-397c-4999-9112-71d9d39f83ae";
    final int maxCount = 10;
    final int maxChampion = 519;
    String tier, queueType, tierRank, id, accountId, puuid, nickname;
    int tierRankPoint, wins, losses, profileIconId, revisionDate, summonerLevel, i = 0;
    float winPercent;
    Map<String, Object> returnInfo = new HashMap<>();
    Map<String, Object> championMasteryInfo = new HashMap<>();
    Map<String, Object> recentMatchInfo = new HashMap<>();
    Map<String, Object> recentChampionInfo = new HashMap<>();
    BufferedReader br;
    String inputLine;
    int[] RecentMatchChampionId = new int[maxCount + 1];
    long[] RecentMatchId = new long[maxCount + 1];
    String[] MatchDataMoreInfo = new String[maxCount + 1];
    int[] championPlayCount = new int[maxChampion];
    int[] recentQueueType = new int[maxChampion];
    int myKill[] = new int[maxCount + 1];
    int myAssists[] = new int[maxCount + 1];
    int deaths[] = new int[maxCount + 1];
    float myKDA[] = new float[maxCount + 1];
    float championMyKDA[] = new float[maxChampion];
    boolean winOrLose[] = new boolean[maxCount + 1];
    int[] championWin = new int[maxChampion];
    int[] championLose = new int[maxChampion];
    int winCount = 0;
    int loseCount = 0;
    float recentTotalKDA = 0;
    String queue[] = new String[maxCount];
    JsonParser parser = new JsonParser();
    championId championId = new championId();

    public Map<String, Object> returnUserInfo(String user) {

        try {
            user = user.replace(" ", "");
                String apiURL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + user + apiKey;
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                String userInfo = response.toString();
                JsonParser jsonParser = new JsonParser();
                JsonElement element = jsonParser.parse(userInfo);
                id = element.getAsJsonObject().get("id").getAsString();
                accountId = element.getAsJsonObject().get("accountId").getAsString();
                puuid = element.getAsJsonObject().get("puuid").getAsString();
                nickname = element.getAsJsonObject().get("name").getAsString();
                profileIconId = element.getAsJsonObject().get("profileIconId").getAsInt();
                revisionDate = element.getAsJsonObject().get("revisionDate").getAsInt();
                summonerLevel = element.getAsJsonObject().get("summonerLevel").getAsInt();

                returnInfo.put("이름", nickname);
                returnInfo.put("level", summonerLevel);
                String rankApiUrl = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/" + id + apiKey;
                URL rankUrl = new URL(rankApiUrl);
                con = (HttpURLConnection) rankUrl.openConnection();
                con.setRequestMethod("GET");
                responseCode = con.getResponseCode();
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                inputLine = null;
                StringBuffer rankBuffer = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    rankBuffer.append(inputLine);
                }
                br.close();
                String rankInfo = rankBuffer.toString();
                JsonArray rank = (JsonArray) jsonParser.parse(rankInfo);
                JsonObject object;
                for (i = 0; i < rank.size(); ) {
                    object = (JsonObject) rank.get(i++);
                    tier = object.get("tier").getAsString();
                    queueType = object.get("queueType").getAsString();
                    tierRank = object.get("rank").getAsString();
                    tierRankPoint = object.get("leaguePoints").getAsInt();
                    wins = object.get("wins").getAsInt();
                    losses = object.get("losses").getAsInt();
                    winPercent = (float) wins / (losses + wins) * 100;
                    queueType = (queueType.equals("RANKED_SOLO_5x5") == true) ? "솔로랭크" : "자유랭크";
//                System.out.printf("%s\n", (queueType.equals("RANKED_SOLO_5x5") == true) ? "솔로랭크" : "자유랭크");
//                System.out.printf("티어 : %s %s %dLP\n", tier, tierRank, tierRankPoint);
//                System.out.printf("%d승 %d패 승률 %.2f%%\n\n", wins, losses, (double) wins / (losses + wins) * 100);
                    if (queueType.equals("솔로랭크")) {
                        returnInfo.put("SolQ", queueType);
                        returnInfo.put("SolQ", queueType);
                        returnInfo.put("tierSol", tier + " " + tierRank + " " + tierRankPoint + "LP");
                        returnInfo.put("winOrLoseSol", wins + "승 " + losses + "패 " + "승률" + String.format("%.1f%%\n", winPercent));
                    } else {
                        returnInfo.put("FreeQ", queueType);
                        returnInfo.put("rankTypeFree", queueType);
                        returnInfo.put("tierFree", tier + " " + tierRank + " " + tierRankPoint + "LP");
                        returnInfo.put("winOrLoseFree", wins + "승 " + losses + "패 " + "승률" + String.format("%.1f%%\n", winPercent));
                    }
                }
        } catch (IOException e){
            System.out.println("이름을 입력해 주세요");
        }
        return returnInfo;
    }


    public Map<String, Object> MasteryChampion(String user) {
        try {
            // System.out.printf("닉네임을 입력해 주세요 : ");
            //Scanner scan = new Scanner(System.in);
            //user = scan.nextLine();
            //String user = "B1oSs0m";
            user = user.replace(" ", "");
            championId championId = new championId();
//                String champion = championId.idMatchChampion(43);
//                System.out.println(champion);
            String ChampionMasteryUrl = "https://kr.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/" + id + apiKey;
            URL ChampionUrl = new URL(ChampionMasteryUrl);
            HttpURLConnection con = (HttpURLConnection) ChampionUrl.openConnection();
            JsonParser jsonParser = new JsonParser();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine = null;
            StringBuffer ChampionMasteryBuffer = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                ChampionMasteryBuffer.append(inputLine);
            }
            br.close();
            JsonObject object;
            String ChampionMasteryInfo = ChampionMasteryBuffer.toString();
            JsonArray ExpertInfo = (JsonArray) jsonParser.parse(ChampionMasteryInfo);
            for (i = 0; i < ExpertInfo.size(); ) {
                object = (JsonObject) ExpertInfo.get(i);
                int ChampionCode = object.get("championId").getAsInt();
//                System.out.printf("챔피언 :%s\n레벨 : ", championId.idMatchChampion(ChampionCode));
                int ChampionExpertLevel = object.get("championLevel").getAsInt();
                int ChampionExpertPoint = object.get("championPoints").getAsInt();
                boolean chestGranted = object.get("chestGranted").getAsBoolean();
//                System.out.println(ChampionExpertLevel);
//                System.out.println(ChampionCode);
//                System.out.println(chestGranted ? "상자획득 완료" : "상자획득 가능");
                String chest = (chestGranted) ? "상자획득 완료" : "상자획득 가능";
//                System.out.printf("숙련도 : %,d점\n\n", ChampionExpertPoint);
                championMasteryInfo.put("most" + i, championId.idMatchChampion(ChampionCode));
                championMasteryInfo.put("championLevel" + i, ChampionExpertLevel);
                championMasteryInfo.put("chest" + i, chest);
                championMasteryInfo.put("championPoint" + i, ChampionExpertPoint);
                if ((i++ ^ 2) == 0) break;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return championMasteryInfo;
    }


    public Map<String, Object> returnMatchInfo(String user) {
        try {
            // System.out.printf("닉네임을 입력해 주세요 : ");
            //Scanner scan = new Scanner(System.in);
            //user = scan.nextLine();
            //String user = "B1oSs0m";
//            user = user.replace(" ","");
            String RecentMatchLink = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/" + accountId + apiKey;
            URL RecentMatchUrl = new URL(RecentMatchLink);
            HttpURLConnection con = (HttpURLConnection) RecentMatchUrl.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            inputLine = null;
            StringBuffer RecentMatchBuffer = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                RecentMatchBuffer.append(inputLine);
            }
            br.close();
            String RecentMatch = RecentMatchBuffer.toString();
            JsonParser parser = new JsonParser();
            JsonObject RecentM = (JsonObject) parser.parse(RecentMatch);
            JsonArray RecentMatchArray = RecentM.get("matches").getAsJsonArray();
            JsonObject object;
            for (i = 0; i < maxCount; i++) {
                object = (JsonObject) RecentMatchArray.get(i);
                RecentMatchChampionId[i] = object.get("champion").getAsInt();
                RecentMatchId[i] = object.get("gameId").getAsLong();
                recentQueueType[i] = object.get("queue").getAsInt();
                (championPlayCount[RecentMatchChampionId[i]])++;
                //System.out.println(RecentMatchId[i]);
                //System.out.printf("%s 챔피언 플레이\n\n",championId.idMatchChampion(RecentMatchChampionId[i]));
                String matchDataLink = "https://kr.api.riotgames.com/lol/match/v4/matches/" + RecentMatchId[i] + apiKey;
                URL matchDataUrl = new URL(matchDataLink);
                con = (HttpURLConnection) matchDataUrl.openConnection();
                con.setRequestMethod("GET");
                responseCode = con.getResponseCode();
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                inputLine = null;
                StringBuffer matchDataBuffer = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    matchDataBuffer.append(inputLine);
                }
                MatchDataMoreInfo[i] = matchDataBuffer.toString();
            }
            int[] myParticipantId = new int[maxCount + 1];
            for (int j = 0; j < maxCount; j++) {

                JsonObject matchData = (JsonObject) parser.parse(MatchDataMoreInfo[j]);
                JsonArray matchDataArray = matchData.get("participantIdentities").getAsJsonArray();

                for (i = 0; i < 10; i++) {
                    object = (JsonObject) matchDataArray.get(i);
                    String tempSummonerName = object.getAsJsonObject("player").get("summonerName").getAsString();
                    if (tempSummonerName.equals(nickname)) {
                        myParticipantId[j] = object.get("participantId").getAsInt() - 1;
                        // System.out.println(myParticipantId[j]);
                        break;
                    }
                }
            }

            for (i = 0; i < maxCount; i++) {

                JsonObject matchData = (JsonObject) parser.parse(MatchDataMoreInfo[i]);
                JsonArray myMatchDataInfo = matchData.get("participants").getAsJsonArray();
                //System.out.println(object.getAsJsonObject("kills").getAsInt());
                object = (JsonObject) myMatchDataInfo.get(myParticipantId[i]);
                myKill[i] = object.getAsJsonObject("stats").get("kills").getAsInt();
                myAssists[i] = object.getAsJsonObject("stats").get("assists").getAsInt();
                deaths[i] = object.getAsJsonObject("stats").get("deaths").getAsInt();
                myKDA[i] = deaths[i] == 0 ? (float) myKill[i] + myAssists[i] : (float) (myKill[i] + myAssists[i]) / (deaths[i]);
                recentTotalKDA += myKDA[i];
                winOrLose[i] = object.getAsJsonObject("stats").get("win").getAsBoolean();
                championMyKDA[RecentMatchChampionId[i]] += myKDA[i];
                if (winOrLose[i]) {
                    winCount++;
                    championWin[RecentMatchChampionId[i]]++;
                } else {
                    loseCount++;
                    championLose[RecentMatchChampionId[i]]++;
                }
                //System.out.println(myKill+myAssists);
                queue[i] = recentQueueType[i] == 420 ? "솔로랭크" : recentQueueType[i] == 430 ? "일반" : recentQueueType[i] == 440 ? "자유랭크" : recentQueueType[i] == 450 ? "무작위 총력전" : "etc";
//                System.out.printf("%s 플레이\n%s\n%d킬 %d데스 %d어시스트 KDA%.1f\n%s\n\n",
//                        championId.idMatchChampion(RecentMatchChampionId[i]),
//                        recentQueueType[i] == 420 ? "솔로랭크" : recentQueueType[i] == 430 ? "일반" : recentQueueType[i] == 440 ? "자유랭크" : recentQueueType[i] == 450 ? "무작위 총력전" : "etc",
//                        myKill[i], deaths[i], myAssists[i], myKDA[i], winOrLose[i] ? "승리" : "패배");
                recentMatchInfo.put("Cid" + i, championId.idMatchChampion(RecentMatchChampionId[i]));
                recentMatchInfo.put("queue" + i, queue[i]);
                recentMatchInfo.put("kill" + i, myKill[i] + "킬 ");
                recentMatchInfo.put("death" + i, deaths[i] + "데스 ");
                recentMatchInfo.put("assist" + i, myAssists[i] + "어시스트 ");
                recentMatchInfo.put("KDA" + i, " KDA : " + String.format("%.1f\n", myKDA[i]));
                recentMatchInfo.put("winOrLose" + i, winOrLose[i] ? "승리" : "패배");
            }
        } catch (IOException e) {
            System.out.println("이름 입력후 전적검색을 진행해 주세요");
        }
        catch (Exception e){
            System.out.println(e);
        }
        return recentMatchInfo;
    }

    public Map<String, Object> inputSearchNickname(String user) {
        //https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/B1oSs0m?api_key=RGAPI-0c614ae5-6129-48c4-aa77-64c6614b8c41;
        try {
            // System.out.printf("닉네임을 입력해 주세요 : ");
            //Scanner scan = new Scanner(System.in);
            //user = scan.nextLine();
            //String user = "B1oSs0m";
            String UsernameForm = "^[a-zA-Z0-9가-힇|S//s]*$";
            user = user.replace(" ", "");
            int j=0;
            for (i = 0; i < maxChampion; i++) {
                if (championPlayCount[i] != 0) {

                    System.out.printf("%s %d회 플레이 KDA %.1f %d승 %d패\n",
                            championId.idMatchChampion(i), championPlayCount[i], championMyKDA[i] / championPlayCount[i],
                            championWin[i], championLose[i]);
                    recentChampionInfo.put("Cid" + j, championId.idMatchChampion(i)+" ");
                    recentChampionInfo.put("championPlayCount" + j, championPlayCount[i]+"회 플레이 ");
                    recentChampionInfo.put("championMyKDA" + j, String.format("%.1f ",championMyKDA[i] / championPlayCount[i]));
                    recentChampionInfo.put("championWin" + j, championWin[i]+"승 ");
                    recentChampionInfo.put("championLose" + j, championLose[i]+"패 ");
                    System.out.println(j+"번째");
                    j++;
                }
            }
            System.out.println();
            recentChampionInfo.put("maxCount", maxCount);
            recentMatchInfo.put("winCount", winCount);
            recentMatchInfo.put("loseCount", loseCount);
            recentMatchInfo.put("winOrLose", String.format("%.1ff", winCount / (float) maxCount * 100));
            recentMatchInfo.put("totalKDA", String.format("%.1f", recentTotalKDA / maxCount));
            System.out.println(j+"개");
            recentChampionInfo.put("playChampionCount",j);
            System.out.printf("%d게임 %d승 %d패 승률 %.1f%% KDA %.1f", maxCount, winCount, loseCount, winCount / (float) maxCount * 100,
                    recentTotalKDA / maxCount);

        } catch (Exception e) {
            System.out.println("aaa");
            System.out.println(e);
        }
        return recentChampionInfo;
    }
}
