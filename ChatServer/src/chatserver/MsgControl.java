package chatserver;

public class MsgControl {

    String[] parcalar;
    String str1, str2;

    public void stringParcala(String text) {
        parcalar = text.split("-");
        str1 = parcalar[0];
        str2 = parcalar[1];
        islemKontrol();
    }

    private void islemKontrol() {
        if (str1.equals("xun")) {
            ServerMain.clientList.add(str2);
        } else if (str1.equals("msg")) {
            String kime = parcalar[2];
            int sira = ServerMain.clientList.indexOf(kime);
            System.out.println("kime : " + sira);
            String kimden = parcalar[1];
            String mesajIcerigi = parcalar[3];

            ServerConnection sc = ServerMain.connections.get(sira);
            sc.sendToClient("msg-" + kimden + "-" + mesajIcerigi);
        }
    }

    public void yazdir() {
        for (int i = 0; i < ServerMain.clientList.size(); i++) {
            System.out.println(ServerMain.clientList.get(i));
        }
    }

}
