/*
 * Autors: Oscar Dominguez
 *         Victor Lopez
 *         Cristian Ramon-Cortes
 * 
 * Assignatura: PXC 2012-2013 Q1
 * Projecte: DNI-eLection
 * Modul: Test del sistema
 * 
 * Versio: v2
 * Comentaris: Crec que d'alguna manera s'hauria de poder votar amb signatures diferents
 *             Es podria mirar de fer en HTTPS
 */

package apptest;

import java.io.*;

import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;

import java.util.Vector;

public class AppTest extends Thread {
    private static final int maxThr = 40;
    private static final int incr = 5;
    
    private static final String errMsg1 = "ERROR: Cannot connect with server";
    private static final String errMsg2 = "ERROR: Cannot go to sleep";
    private static final String errMsg3 = "ERROR: Cannot restore data values";
    private static final String errMsg4 = "ERROR: Cannot write file";
    private static final String succMsg1 = "INFO: Going to create threads";
    private static final String succMsg2 = "INFO: All threads created";
    private static final String succMsg3 = "INFO: Thread number";
    private static final String succMsg4 = "INFO: Finished and got this result --";
    private static final String succMsg5 = "INFO: Finished within --";
    private static final String succMsg6 = "INFO: Correctly loaded all data values";
    private static final String succMsg7 = "INFO: The dni used to vote is:";
    private static final String endMsg1 = "END: Main Application finished";
    
    private static final String urlEmitVote = "http://localhost:8080/DNIe-LectionWeb/servlet/emitVote";
    
    private static Vector<String> dnis = new Vector();
    private static Vector<String> xvots = new Vector();
    private static Vector<String> signatures = new Vector();
    private static long suma = 0;
    private static BufferedWriter out = null;
    
    public static void main (String[] args) {
        /* Generem els dnis del fitxer de copia del cens */
        boolean generacio = true;
        try {
            FileInputStream fstream = new FileInputStream("cens.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                dnis.add(strLine);
            }
            in.close();
        }catch (Exception e){
            generacio = false;
        }
        if (generacio) {
            /* Generem els possibles vots i signatures*/
            xvots.add("i0L5vy7YhwmQ-0260Wb14bBqsZUHIWSzJ1ETwCedXedkRJsULP0F2R3t_A8pjVsSSXkCN59SaXKtT_rrxag2M-OA87UFBLsz2FMIwhSLl10qpwk1W-4YMxK8Ucnl1cntueBbogsOMe6I7ccdogp50t-G5JIDP_Oun_9g5Q5eGItn7ZuosaGzArtg213I920kfxGV22jwf_yNDT0PdqmNTdruGMKpLXNq6mXxqAVV4sVYdkjvOeM0fzc9t6IAUVkuI8Vm-2iAEUAwprK91XMOigkxMdYf5AflLGm1yaf9IYSvBQRYP83YXAK02ShASCSC6y5fYygdkuZkzX2hWIxlqKWQGe_3DjHa4HdR3RY_MIFKuPE3ZdNc2w__X5ykzbYnRkkepnjwZXbbeWSDiPqHlM3rPNYxUMtItTVUTzSdGPma8MyHyqGL4Zj4PBpWyi2grItXwk9_Mcu9WLp2lfvJNDvad9taHTJ9bOoGjwXwTJJqbGkh8VRkQY1ABYULIWAnL1OsssG32vUl4xieWQTplEnKtsrIK_KkOetTAs3tUAYo4M1L1Hm-eZuMiGpwFO9rU4J8JAeQIwgGgsiitAwjhywk2Goi122fgqx3jjpkCyoOkUx4gpntHdJ0nPUcfyoLKXs_DD_eAGdn6gkHplstZn2_WTi_Y9DOs7MEGceHxr4");
            xvots.add("kpedFOWHqvf8el1wKfsQFPI5qKhuZ3runJM183KRxyJDFz1c3jpCjLuKIpKdvm4-FfYzXUuLtCuSsF-zJXOZytMqWOjkXpcZ_Dqp3gFvjyqmVQ843_UimAgL7638pAQYRLXlN9yrV9pJF5XRnd50PljQXgCgcu7Ti6KJ0lvCl0wu_8FEDjQKguESgUnUBnturhgALLPEyAOyQsq_vul3d31GCA2K3n8SV-0xncupIeeJur8eqmf86mLs3gGjmkJmumIpBjNbkeD1TMvzxgSl-aq2-KS1RCuNnYy7qPJ-O7yLiXeGiZQf1wGqic2QFsy-OI_dF7kg0BbdaX14geGQYDM-6xAnzbFhLBHoEsLndEC1TZ-g_ZAKsfv8Po2DfWNb1l7TWb7bv4rvPJTIntjIDTx1rBl73icD9l85JgnoRgrx4ZRaKuL8JbQo6wuU604XVcBSvOgW33qSmoZL08WRPRXE-n9ooL77u4YJOe61ffHkRJuWTiO-nTcrKl4mc-aVnCHeD9hA0KmYVBGE5U4UGBko_4q1PWwr7Z5xgut3gWO-5bYKOOOscXxjkoDLelZYwfVz2dhoX_AlDTUmE9lHN8yUbDGYt67Jbn7na-hYiToEvdYjyWst5ynyqFl9hzRPIHpV9pzqkBUmPqITfQDWi8-XuvUQ4ML4kd3Y7CP4xig");
            xvots.add("Pvxs4uSPefK1W2JlU4Vql_eiD4mJvp7E7rD5UA_Cq9_v-gGQU-x-uaiuVLpJJztWGeIXd554cuVGUYQ3-MkEdRYk0Wyg568sc4yOZzscj4TDJmEEDKtbNq1Emc0xSMWva4qTe9FXz5ZP-MfFB03Iq-TVyRA81H9RhHMAipcR6GtxwXFTk9aLQ62cZOcHTynqAezQF4tqeO9ikOacKD47xw1pKLgHnwr8SBFoKXVtjUt0ON1gBJEoqHz9R3RJt_lJ6BcrXiA8k3AMHycp5l890eM94o4ondKqnFmEYdiUoqE23LDlGzzpAmORv-_yp8suvOrlT2IOarAqZ8aezBgmxcHGTJKT1-VtY5maXI3surQdeAaMwZKJ48lHjD1sRZHC4NHigoCW4qrk60wmnNDW7JkrmEQ7d8DQz24kX35Nnpwj1jBW07SgzWGiZ_zdql30eS_UeEt1QodaByJYamscyI7CSzJ8eXxp1AOzFBrV_fyOxItIZUKTydMsf1FaTh3YTJcdtjS6DS3qucE0-uBiic1yrIeQTTj5gdK_1rqh75MFLkC_BdS0XklkoKxbTVcKhG4V_dBAoc3fimMx0WFCBiyIPCXzzgq9p8LI4yYHHumLKy8lzyWnP76pfVTp30rCXrYMgyTUU51pdyhGy_OtFhYNXwmL4UalHZ-JD3-DJ4M");
            xvots.add("kUUaq06Wkbx82mQRiSoGSKO-QDvAON5xaNqS2OMWRLSEsVsfoPm4cn6E2GoP8lu-JjPciUyGqjU5yiokKjDCSKYpBTiq5qaHIRib6GLwVT4ufY4fp1mAJQYhOhgnV4YvlU6RMSHhbKDm1aD6jNEEqeb89St3_1bF9jh6NXDatHsKDaH-oeD4PPxvbZ2mUBNMjka7soFL2LnsSGN8s3zlHm79yi46zusAvqb2wSBltjn_PMZ5KE04W-LuDVw3kZE9AsdFFLdyC8VKLjXrI0E4IwfMbIfnQXtGejlubkvpY4fBnv9ftcE8u87BrKi0dNKEssJvrKo3OrqihBGzYp_QJWcc_EGwODVtzsaB_apShno5kFElOK2vS3lu_uMLIllf2s1-ZReOBRKb6Z_mBU-r0Gdkgu5Kv1LM2SGTJylNuBj1kiURWJcJlhwbFXxLF6KCr4GmNzEx9KB-zLv_THKIkq8zp2Mh084Ed5_AN8uZ4rxlBAWgcFc65qMl3Bm6_XxR6w1n2-rwzrjHWOB2f5iwMUQqOArMRMgQ-8XlnsV0bQNQ3SeO80bYn9mvsEmz92gKHoo-lpon875ISjnIXSNzfBuisaEZDfoSbarfdzTSLepchwEbCUd0v2M0TnUxmmfj4dZLTe0HUOuF8fK8Q8Vmi34zyozMKM_pw6eLMctTL68");
            xvots.add("yoYURN0tAi6PPuKV2j2B5yEQT06H1nHXEue_NsYGKbvSH2m7E7ogNI-OtjXITvtruLyyLJmxOSmxR-OslyofBsU2rDRtVLYosE35FsYPqxEon84LcAAoeiN599DByjNsyfw1FR12IyFnDAcUEeZpIMO7iBv5e7hbEvUdBW6VCsmGuhrGOd_EYnduQK11_neWnlW9Tsfxqp9j8EEvnf0bEGSkan3eAsIZPf9g6FfQLIES0lVKkvBC_98V2eIWzV2Wbe9xxBYV4MLCN2nxexIJiB8g6e4b0jt5q2CK5uW4V5lFzjR4DhldUc8TUl2v7_j4dHgGllT1Q2bHOgZa1TJMhjlbTtLlP7mB-jTKKLcJ7Cye0Qsh69njDcVPuV6QLb8O2yO_fXcehEe-R5DrTc7x4n6ADvqukDGphClyDVFt5vKWKGZDxS-QIZu25at9q39SPET3DH5zGFlt0Cipeua2jeBbc68GutLGF6ouawUYgvnYRojKOmrvBydU0tdf1qXZvm2Ax9SGNKgpqMdA0NDRLbclqtiQ69WJ7IjpJ5VYD5WNJeHVjiwIegUZrfIFSxT1uG3GYMM9Xbk_bijkZnsCYhYh7bn1Cu7OZ3syDX10d4gMXBTX4PPzX5RimfRLPiRbhOhEPZN59ZqalLpT4LVl3JEzg2HWxLDSbIvlXJb4R8A");
            xvots.add("AqAyLkxCUyf-_uy4G3_WXoEjaO4EbTwpvr12xIefPJ-hPzTleYnpN1QFGYbwVmcxfKBxtH5JZj74ZMsyJRjrBOjZW7RnLiWZTae2vJ-k3EwNgLPLkouRlwcCXicY-vxWyyv31ogsPtsEmASe6cOETQm_UTdIfFmJztQyI8k6aqwepTo8o3-xJzxVeXuEEdW5g1-JmlhMWz2cTzdeKSWdp4FbPyWVb9X8ayywXAC5BG5J2d5dwsJqOMlvyWIAFs07Pph-bwgTEeeTFmZAA01T1vYeMBxdheddzspi39Cv8HkHVG0mNcQBYC4WcgqoSQ8-9kUa4bGfPCNvXM6A7pqkory8EYFxknmxGkHxDKoDq81EnkSpkTefJ4vpsSwUagIBo4LenRwikuDfZj-2QZjxxkgTNLXFH9AeK_EpJHBBkF5zuOV2XlA86qaxKAFjD0jWm-b0y72Xw8uoM7vBSH4op2nLMrhunoV5lKJcdy7yfKx4SYfvMlzU0IQ9XvhZps7dqb3itn7z-7G2w-ym_0ldh51wPvPAh3Xc-20-BpsaW5PGi-aF0VrVXLFY2vXyc51BNraUjktgfksyrCn8vMjIJETK5DqVjme8bb_rRelnh6y8hhIEWz5iiPUZerrKjz2kVNc3vyciFAZVA7iPRAChbnx7i_7F9sE9EJGHUQHdcHM");
            xvots.add("qFYHR1m47_bUfNRhicr1yNQS0MC2-J8r2YOrqvX5k6OuuKaPzrovwJLgeGPXbtXQlvV8V4usaDsyZqE8Kwes62oEDHSorLalPL-JnE-sMk_s7JyrEMvVLpzEWx62dy01NNpK9mqs6ATq0ihQU-QJ2VUukEYPH0kcmbGaWgKOq-Cyew3kEAogPX0rdXm0rMpOeyAjVBuTJZjs90w_12xHbx3BkEv4JAVWbA9GWow9l8mk3W4ayl5yyOTNtZVWtZ4EUNAYkwaMfEIzXUZN0joMAjPEJZY7lTbVvZBoh5MAdm-C3H_ZVW2CzsLGI_6YISqquQSFlQ6tNI3eTk_BHlXK-iAjVwDQhYSX3oOWDq9tCJ8O74aj8HT6-jAkLwsyq14boaLFbqTIUGOmKXZErT4j3fdxeo5Bq1-fXYKw4OJ869MEVgtnaus9BVv6KyLtPmYxPy4bqpa5C9ytKzE8dMxFuFJ5P9sy732wEVwpwsfTlnaVZw5H_SjvoqYNxXCQ063tL8N06LvVDTd86WuX9p6-d7Flhb87IydjfYIfr2i3HDeJmbXkx8laLodL3LxEOQBlG7eotOWLM-PVNALP2f6QQRH1KQHKJD5rEWDtDp94I3aWz70OSr2I23Obon2NenPEztkCoJ21NdI4WcBjAIejVUcVOEw8FIPoEl5YtjWCAxw");
            xvots.add("N-ODXI7TAf0QxW9R4gYBMmVWPhwSFV1fIbNBqxdvKR_tirtahP7fCYcaoNyGDCO9LHsJrL6vfA9zCVdVLBoZtjIX-SPWixtM83GuVDWUDGDFzSY8qR213vl2SfgD4r49fZMrankM-WVhTf_Kmx13y4OZWVGNFZhINDrK3YlY2wI4uVXtklj74wLIBCKYMiiWPLHJ2_aS9m75J4OD5YeFq_Hx2rJ-oa5UePu55xHwwQbVVWI1tD_IF_4tMKpBH13zH-1efqfL0cvh0vPCwK1obOBDZz8w1fErPdWqLB3VDoqyotHwNbK7rz3kTNI53Rx5UI6wSzhXSNt3-7lgJzrTqaFUHMYQOHLoFJYKMPeEwdN-E8t6qZ-Z2hXz3TiXMkBMbvjy1O34mxRw09lJ3vKgc9e635V-o8xg0kzT3r-xeqSw_R1p6c6JpPqe5OLOrWyzBeQ7rBlBWgmPhmisu4PeCS3FbG1ND4DZYVXeAjv4qLr4WpsG_dQc15p9gUaTdpUZ5Eufx68WAdlgLsMtNo8ZAhRVMP0ZtmbRpV0bJMOP6Ozj0bya-LR5nGwWlkLPIYFZT16nLg3mmkYnvDT0MbKyyC0RUH5BEjjuvtk-hk5dYqt8HsaSqotlMe71TRhMIz3SJqQirNt_dUyRvTFyufkvx99erlbaL6ZetcfyYCgOhAM");
            xvots.add("G04KfDIRWU4y6xa3JLeDvaw_0rHU0qR3m_t1NAbKzfFUQ-K4DmyxE6J_rM7xjND7K7urKwrEQr-82tr5ndinj_uf-ioWgZYbQ9XvcWEnrJM0FdZD6mw3PZEsm1Yx0T7MCXi-fCLhCxBDtve5nPLg9QULU2v6cDs8wSOCuo8Ph3E2yaVUr_Dxbm7sI6XR5EYsyu_nlRpJgH1juhd31c58AI9AOwuUmwO57XFAsunHpDyFSvckgyZgLCCbPJmf-bC4GNQP7YahdfPyd0VEa_Y58uJDoyPuiDf1zo74t8EfgT1IOKFA-fYA0IjLTJhxK0wjnk823Dio5UTSdtXaTHs5knZ_gI79SooX61vTIqDDmPCD0eNSAC6wG5cLRs6rQluvpwGH33VySh4s3HbHPn6yeXAb17rrJ_kHQNqAOijvf_VKIjI_j1IuS6Oe-ZgB9d9G1qaoXddryaBv395YZIXdyTKQCAUXm980dXkr2F-jtg_ONGMmWtvrJhL_ghsVZb77fCtnsXFT05nqLy1ojJhTZafjHaWgG726sCJPIC1wWuU83OkXGj2LGEZrSNyuzJcdHyhoM_A4xsh18zAldRhHXXEJ2VA_7iTxiTM3-odbXsJtsqkfOtx36budLn3fNjWruESwf0zn4DYS8m3OUMgMrbcvraYheX1dH0hJaqltD4I");
            xvots.add("oABOWT3ifceHZsuH4VgKLQ1MCO4r53I34mMYvVRzxWsMZtQyA1l9BscKvNrD4yj51OwaEUSwySvjJbI47s8ZOdp4GNpyOhFqGEvMCkK2TzhbOzymKo4aweleZIR0-ie9U5B0R52NApaUztKYCPF5nSuG60frguiC3d8VCL5yctNOKSOXv4vr2dP4rRRiVQGjkkjKXF5E87Jo8H8L9YiMThmB_NNo-aK8zGx00MsL9Zi3w--qCM32iZGoifjVNEYMRqApCSojsgKOYj-ppiVqQh6dQqtwIy3w0ZUVzw9d-wtaSOXFlPxSJJheUDLY6uEg4txHymC-GG6fSPKxFE4Czm-zQ1EiCdaZ16nqV51J6DoiSDXD9xpTqr4YT_8toI9UjrNfhzW1kgpZocQzdRZzQAmVgGhuZGbs85YO5BclkHYoEC8VlJwS51aIMpiCbqRGN6jEwLR3E3uvEbC3lh8Kz6YfSwT-f_6KIxP1soFlqWlBSrMmOo8RdvwObbCMqWO8q2d8cgkga8t2LD6lXh2a16mhr_iQgVsyfCLsUDE5s0TSvXPRH1AJ_e590_TlxYsDkpoMcd3IMW-3yPfLxawTrogjRupzAXyTxBNEAR8g4ZD5-60TYhUIaUEZMJUH5GgXkWVhkO8gwadH2G5FKZuNwJR0c9Yj0xKaEQ564hgp_fM");
            xvots.add("bIE36iil3VSHkAB1usmFHiLab6Gy-xuqcYr90nnTZHLtak6Jiz1VWpqUYPTeF8KX3bDE68TpYPBIok4d7MGrfb-eZTNTlhKo9iL2nd75biQP8oGdRghQvv2Bo7VufPa1kW4WNovBJ_GMJdEoRZPhFIwSoXb8L6PtASWcFHwBdOqbgO1Di1U2GKc77ssN7Ob36J68fjdr8od6ZLod5pEDuEblVNtOxtrk5gHT7xp-tRoNfV-raK8IHkDfvhBRbjXWLES0F5vjoKxvslEIUnsCEixoykPm-IiWJHAEJed-qoRa9-pni8hbFiPejh2iO_yRKMJ4yxoZpeDreJZ1aL1Cbw5xIZLMr96c1XlHd55MVPN1T-i0GDwvGTU-KaIcLloO7cSZrCPChs9pz2nDiGJcZMnduEvg386LiPh8_N9X4egperpqE0St3sQN0mYFAYYrCUyg4iIXkmWUlnRir6vGtkycSHJR8b8UUU123N7EuQ_7GdBk7Lh6iAg-LF6dUnREVNpoDrHEp-qGvo5ZzcQ1OYoCyqsjWxfJdEdQPch8jUnK_32SKQ5tz2gzR7lSTPY1TbeGw663ww1tW8uvYDWZgbrRnqQWdUIdjA4ki248hcI6x90M1zztwEiuTMRixFrc5zkEOL6tvn2Tz3YvauanyYaUQ4dTT1uRwk8guux3lr8");
            xvots.add("xtH61D-CYOmWSmboAcj3PzBT1uJLmWFyhNJ1A-OjnBANKPmgD6jks-N8ZxDHNt6YqJsk1pztILTnpNmPkfD-IwMfjMtNGdkpSUAUneLuAs_9vTkP1cOxdvGVOx6SNPjnpzZTJzqxPEzv49r0cNbsmso6MVu05jXE9U6et7KbuVIuFIWkhzWCx3AOSz8ZhK6gxIEQEuNzpZEsIgTtOl6RNy5o88xU9I3X8r-onxhfzdd3pgDYPGs76msyN_ibupZEfTSvTIXDwMF37k-7uFCV9MtdN4taCksQjKP34pMJiNNIoiu0j_WXIS5BZGEY3pMlCmh7zhX0ubYltt78uMc_WnqFAm5xUFFLFPG0WdOBqrAOhKy5Re6OEvWSeyzsclS2BQ9aGL0M7_CsaYJK6GP04h4DToY_Mr_P4YX-1IlZkDnX-dbBsWzaqIOfn2TaXLYvCVvC4AwnsTm6TdJFrEtT7YbckM0B-0IO0ikKB8Wrlxi82vTxYZNtGCluIYODBEpOND_5FKssufVfsF0hcui91gxUz4yWbTYs9MblpZ6JM1Jd8e__CHisi1Ib0qs4T8VoAyfM3m6s3VHQwZwxxts-pkkg8Kh0hO4pkTw7sFy8S30I_nlgEXSD0j2FBxKSwhBvtsRYUZSM2gqxvfNK7Lgjy0uOtmOwzeA95AhTD9PjQxw");
            xvots.add("xRJ6TuYwHIrJirIVBIGbPDf0ORt0zn5h3I9OYnAN-e3DrG9tI_SNwj3am0x3TQXvmeQpRz3fwJj8PopjAOqNEGGzBAHsRJfQ2KQM6KuHSia2TnvYzGud-LDR_Ncg_-FOIhzRAofgIljt0EBnaovcZNrql78ZNVnjF3TxNmmhItF-a_6TalhGZtgiNP-xKi1d1ho0BJVZZQMdIAANpihaKDd1WnvZ3DFAN_EIsrBlDtLoz88hos_ZhocysOmUg-4U_QEP-dqXlEjYB-rx5ufMV7UlMKxvSD3KAnZkTtKSY6K-Vj02gTG7C3qsJOLUuKP0XqigXUvvaipx8LCyx0OPf8YzNm0aT6EH2fa5ViNoF2JVjkbfBBTZPPzirBKj1wMJ3j_PZy9KyQO6VycI5FSEAxlTst7zuz0GQ3E5gIXU4_VEyA5Vs_cm5swxs4Rqcv2ffQjOhoW-r3yZI3NO1jHshadMINUbDwd2bGA0DlH0PILgspLRUI2mgE9AjI8RuUqASN2ich9MKTeOymzxZfNIDEYGIUsYnJFxgP1rWZTJECv1JGp5csb6NTIzxIQS8TE_0KX0IUX3FcPq-ObErTD1c3jSHbNU3BsfcAw7HghMCdHkpUf6qbS1rbLseMfMQ8neAbPBsiHLkO7Jd3Zlvgk_FG3CwkBgamdP6_76xMpgbW0");
            xvots.add("m_qPUCWvEPIUcHl2xF1MeZZ2ddylOFY49-eIgJ9BL3PIa3kL4qDuaebvWZcaxZa01zJ4heWm04V6bD1u9XTxBUQhqnoLzCYfLujxydsQ-KshNCQIv0JGBMRCX6e8Cjmp_KcfLQlVP5UtR1tlFd6FNMdLrhzmvJ7QMlm44jAWl7vozJAI2Bs44A6ZOeGB3rD5AODsS7FOmqfMQjqgSQrLDZhqU7va50konnPnsPKR1ajjzwLAXdp6NIyM0DFTwTUCnV139rorBU-4bjExbWfNLwRKzKrRvPb8VnAWXTIfJg1Eb4FzViKn_-CV9NHbvPLWIYU5k2n82yak9j8bZm5uzkH9I-Hu1UgEYpAnytqR-Vge9T7bL_WeEYLD94jgIglIBw9x9H2YtNy904xkr2gA9zKR0NmiukWlkak2p37nuUWrxxw3cmta-bjhrLj_ZWQ6jmQ85sO0teyaZqMab2aPFwTV5a6WMu9elFEzQUqPlB_TnpTJn2JGqI3xVbAxiOAOGRqsZNIDLsed-POgs_RZWAugS_YXgWMABrq-z0HDPAJDb_uJ00GNVReX6PMOGUkH8dinshrgPes_VZ8Cmzq_1vg6fwPjOIAYzVw9hbugOUOlvvTMZLKSlBDyUDH4b-SJ-8zkBTAIxz5TQ1mFHPRY5NZ1630zv0w6IX43oJDA9hA");
            xvots.add("KUujtbj1kmPRlV_ePDafKtNnNHOEop0muSIklVtUkzo7ooAyoPMNUs4qLwDxDjnj99RJUh9s1a5YEkHxUfQFGy64rArgdqarWsOmNHS70n2QrfZ5ooK2c_kU6ef1ipAMGT4p0KKr0qGQ59a1I-ifKP4LT5ix7FeOQo2m82RCtWfLdeEjc0n6ho6vQbdDhGu0fJWmlA4kH6NlZj5nhQkEkRFf0FO-qjbTunb-DKqU6h5d1PBnJscgJmhAThH7eEmkeMjbeDwUh3r3LVMDZzwa5SCAeUxr_6BI_7xpdG9BdsZGJ6yeeFzZzceKmp_q5tPYqXS6ewrozM3ybyKvvoyGQNd17dxImPRXfLaYbp_jUsbH6vj7V6lbuatIHxXpPnLpcS3Z-GEgVlAOSHTZXwvPxqpXtcX5ogNEeGoR7Dq_ir9vYCDWiXWb7607WsAD5RS9C_FgIz5e1c_996ZVio4lkuhx9wWX_-vei4JozNMc_mWvKmK3OUrlr6YJUeEPXkfeLwfO_RvYQZEJutUqOkfuK8S77tAOHuZY6m2njTTPNdrVprVzYt7Kvu4bOS5COOmPIA5YcmE4jFsgS1UifwJmeb0ORTpMylaCuW6B29fm9QySfd1NAofhSAFz2XTWdeG2sI7feTpoZw-PqQcf5oJUrN-HB1n7GC0HceefAEYdNjk");

            signatures.add("nqTQOOHYI1itHX12oUH13JkHiAH-xus9PqKC8UIeN-Rax35gW6oengzE9vbdmgXqTp4MKYDFiLVgXUpFmNnHQIy30d2rrwHfeSq4SyowZwCeFQsxfC_RkRDOLHkKkUaGQKSURJIPh1OVvqe3e9BfMzjZ9CKIR36Kw-4L-PGJFH3tmWP6Qg3sdzX43e77wFWF5cZvgZZzur7N5446Xal3WM42Q3G7quUH2IhZqjpA-RZ1uBbgYk4bOv5l7aOraDwT1z3S0TAhQ3QXirZTsGAT6RpAl3bsu5s9qtZIO49zWkLB2Ely6zx9pvtfnNh8iSxeDf9vTat2Vf5DoxNg1UGKQg");
            signatures.add("dVz7r9PHFuoQQAs72TducZbFijx7DFbmPrv4-aei3peCVLNMb-3pX66Ze3Gx5Q2Q4vIdoyS6RCB0NQYSlmJNMGVXTjZygcV2Xk0yPLQreeOp1D_bnlB97aYPsp0pmi6fiDzbORzkziyr3--NpQh8TxbCy_FYgWYWvvEmzb2OES1pQhNGJEehKZwBRTqYUP4hOhmK0Nn6pnuLWAS4DoIoFDgS9YbjUd9_l0Rt-FASlom6bHPNUhm4C2gBhOK6cERSJ1_IWU21qO3mrVfXMR-aun3v5ehQyaAVjPytpE-S8bmhI31_I9fktth3j_O5-BSAQYklbvg4b0vvq03KxiRSBw");
            signatures.add("HNxSFJJT5HMMQCz5plJgPjGwUklrx45kYRa9XyIvynraEC08OCfzDBKIdc0oI1AKUa9u_q1lxmlY_3KN_k4ByzXR2vm28zeNKXCDr__W27be4J16IYIqPWtYtBsCpZmjH6l9B9gHu0rurUdw0E4mGmlObtfswkngN5ZbVXGjVBfz1tTWsDU_YvpZJHJ6PEgRfJVmA-VuTfapeLvyhE_aW9YES9c7_Fyo62ikyyVLjtJVzhZYlsUE5Q2CP3l4H2yD1uJt3sKSVW2rnURVjK9gkqF3_X5K7NHgX-NFRW1DabAANeQMjpCyyJngTcSCutz49HyN7KVeY5Oj_a5hFW13yw");
            signatures.add("kN1H_uy8W_FmkDO3lmwYyWcXgFmmrrdeNiau8Gcr2gfPAIjOefH0tBVvSzMjvJuRFdlo44nIEk0I3iLSd0AwhYW3qKpxYSIkzUNyLcc5w-J5i-3hnrvKAyEig4sd23RLVcPim-VvAk2AKsCP6xz39IxgBbwKDqBBFJooZyjf6YJbpofJCxWYZFhCwxDReHOxztBD9VRRI9ukjSTiNAQp9zleKZk0kON3zRFF7IgYm2TzkrjbKomDfVzeWaP6ymua7WEoQfdhJUyl9ubj-gNzliQZ9xhHciXp803mwxhzBYdNMP4tkC2EZPcudZtu0S_b_4kAYHzQufX462oBmf6YMQ");
            signatures.add("RyRLd4EcLOiVayrY8YIvOGXjg7q9q6iWqI6XZALoNeVsPy3UlzJKF17RMQ4FVpCLbLdJ96n1bOM5C1qqq3HJeonGd8RYWds64YPO__x19J8jMsFHHo6_xY68v5M8zvfmQ6q-GEgb9mXo-25qm6txTLp4ny0TSFdlxYiMKN9anu3ujm_QKW0UL1jSAxTh_HsV5JoMyDxB78kwJ5BcpLsmcmFcU8ekGdiCP4ZQaJ_fZivP9poepk66hxu6SprhrXNYVU4KFI31Fnjhwo0_0xSmhP3QZQmE8H0Uazhprw3SE3s9TtaI3Z5uRWKd0XwG9YovsRVWuzkBi6uB4TvDLuX0hw");
            signatures.add("Cx1jcn-fZtG3NVDeolY8viTeUiKVzpo4kjgYMJi9uJaPMHZAPWrtIqFOP6d2aqKHpDrNwGDX6IODedqYQLqHHmEcoKY3OaF7AaHQ82JplzaKXqZLE9EP5M2QSKkBPo4DIjl6Bf9k7vyRqctuuUi1hsKT0GqU0y8uJ7zBkFOysAtm6Y2HPx65E0dMeImB4sZT_Z2TbWQ-eQxGbe2t-Ep4B6ocxjs58OmR1eqll45BKJ1d3EcQNbrXctJDVWoxWPBEByEwP24oWq525TXUFMjcGWsmfylvhZXOaZNiT7H8-gFiKk3fZ4SPCBN8PdvuTlrt8v-wN0CRrjA9y1uCwGix4Q");
            signatures.add("O7Ee7A4bzAaRlQPLhP2ndoybCUtI3_ErC5OXgC0EyaMiHoBGsrBSXTQqi-yqxd-f_mR75VSjlNTmJQZNEaf9eXBPA7TgiCVydikJ4AQexrmePIvLEOVxW0xrGMjpqMhBUZgCSXT7UFnTZq97er-DGkOhME7HMFJK1pfl650gLPxIW-33WlktuxaHuuzkZzuuMhdjW9LI-1_Nw98Y7EcshXhmafmDO5g10o0niuS-u5MJV16UABbpge2cyCteahJiu53v5ow2pMZcB210Y5FDEaO0FfmnbEH4O8CPHonEb9vnHg5U9k4WG7SlnHONqqX6XC6mTJCykWfEQBuUo6NqKA");
            signatures.add("RhW-G0FNghJ87PjV3DtwIBp67OYYAwsUl-EoVmrGDVBX1M2Bw3wnNMVJCfBdPbPWWLtAZ7AXi8GfldxcwaFgK6GlK6r5vWdIqhilQOx4Em5DQByjOdlDKE7yYbUsuD2es-3OJmfDaMVLLR_VX4BFdQvIdsK60g2D3FZ36dXCJKT3fnt4Fcyk5irV7TLqR2SUlM04r72DvuUk6FW_1_-WgaosgXLgq1BXX2VL3yYrwxFocbQ1Hw9ru1jT5I1XPy61FilsGif7RIc1k1Mzn_kUCZLska133Ncz35ENpwoKNNPIUtUnCTtvWRLsJcxejWnmmxHW1g5hKxNiwl-4SnSe4A");
            signatures.add("TbIpY7wPKxPQsZGEMcaWcnMTkkz_iUTqa-3-GicnKGsaxRQKR7rlZTmRfHq_gxTzYLpz2n25nNNvtSw6LgyI7YThQelQAsJtH0oh11bOSBj3VCI9U-5ulnOnjCU_bsPUG1gaT4UQwEFp45D9WBS71aacr0t1y4xFOnDK3RNFGLVwsJ5cSf4J-KiRZKxJvr1YT_em019hCsX7Z5hBj3WI5-6fpEJeQX9iTbD9kCw06R-SaNGhTN5NeOrcxhV2zqWp972GoEYjLEayn5zNYYEANCXs9BlldAkXsMOwg6TToaNGwNdvTi-OEvUIMIZcBnE9YrlR9SWHGUxYfZ8nRSWDOw");
            signatures.add("DJ4IN9I7e_vAL3LLsB9qloXBKNh94L1e82W1BYnCWjswmhNB6QPyfXRMYY4IadyYDz9Kf4QQyBuw7ATebVytqv9l2IT4UCQ7H4NinPApUG9SQRa4KTBrj2_ZBHqYkepopQ5lhY-tDaOJYIs7tBSvit8xZmtp-mRlyuScdZsPVy6WwTPsm6jxAHdI1Tj22Sk4tzLfE7EQljV09-9AeAz-UPl70k1DgyH85uyS6NGjiPpEC6vnyyYhVrgQDCQp3rfzdxRq2nke6sjV52aRiZyzn2EvKohKHNIUzIIY5L-xygGhhHBuNCUCjFPUV2MCxcotnyagVvdzBjmV3poVA471Rg");
            signatures.add("GOr5tO1ahvW3kzs1tnSm7alj4rkIMJcXxlTAnfOC9HF8O-7yGCow80YBnuql3dxlyBuhgj6ToEkPh25SHj395q1ZrzM1SfavwMg9Q9ztSj_n3jsQIFMRfIl4vh74S8jDfqEoROnO9ktaFHHBn_xB0uWlMqCg1bJqmGZzsqLAs6pHSB-L_G9RAafU9bgJKfExL9arkmXo29FO1r4Ck6lMnvE3QHlr2oZuCfPgWodq-GeQnYBAn3wdq4rC56L579m_wwuJ9oQ_kizM7B2IDJtmGIvgbc9kAKFUZgQXxfSPj2DMCCjxr6W13gJyLNv27gdoD5nHzBlxxA-DiKUa6LfZBA");
            signatures.add("mRaKYefCyyGi9a1ICwewpOrFytwGnJNw43K7iq5mvkmj3nzrpdVNRK_2JFWqhaQ3_xRdpHj6_V5FlYCYYTLsWeEGQ9039guuch6AsaJhoU1r0mDcgQRWB7recP82uFjE4ENVVPEkKDKg9fB0dz-ZPeEsfZ9X0x7ZasmuRPtxdzvGlbP74Yf7MZZBoWtplpKIW01E1e5bmiWi7UB7Z73JySNOLgr62_OwUu9ELaMQGPvAIcFOX_B_gENwIJ-xlID9xLWCE7GNF1jLkMNVAGA8wX71uYHWF4nbv3WpDO0FjgmhI2Dtp2RqpqhYpZb6JnTIqRG4b6scCANSQOeOUe3Zqg");
            signatures.add("QBsr0bZar0Ynb0V-2zJR0h8pnsdrLlZsTpaRf3APCeV28s814I2Q1LUSQW4KNsK0XJ_iO_iAJWoy4QdCAq3IF_sYR7-mHwePfSXoMDPwrhL4C4_0Lh4qWrGru892J3kzZZ4GY2GettSuYsHyI0uP58iaetbPtHN6FJZdrwzAb9A75rdqj3iPZGPh5UYK8tgZ-2dWK6ybh8u0XA5Ca4Qg_UaCmufOE4184yFLQsVX7XQZuDPHLzxLp4yFJcqtDBNtNBY-JF8Yc85BtJoa7-OhTg915dntcQMpDiKCbUhRXXjhmYVnnPB7RlUFCMnMYcs_hKg6u5YIXoIO_s3Rp7GjpQ");
            signatures.add("NapJMKkesWnX48ZOmTAbcZLRWuKV0oPTjgO4pb2FBaMCOv_3UngFFn6EAs-NNs6qOmET7T__Ia7fmTOR6LZOxb5TKdIBegz0SoVK3gAiNnDcWV8lAEGGubIGBKuCLZJuOOC2VP16aSMa6Bq2cWuIiJlZRbwm09U_D5iL1XAIS_b6sMnx4BvoJL_ExTAlJ7Zi_CeQbOFCfI5i_sL311WhJke-FgigNoKHiJE4HK7tEX12upCLJth-uCbMPdCR8gfzw5-WsOTU3-motupVLhTAg-e3RE-tOl1voF_BFsYC9OQke-gqjSw_FoIX_7gwJCiYx_ha5_BqVMvRAnh7GQTEeg");
            signatures.add("nM0B4UzgAgamdTuB3-YbwbINhx8t-29VlTKx9WUHVGLqGfU_wlWbcWQpgpLIHh2G2MWZyDDPuZUayec5nQBM5CY33VTvaHe8J-tcxcoBQaycWjETIwpVqtKDpr5eguUXaJO9ZCvLVdj33YDo9BLKd-wZUjJ6wGFYWnhz1cNG0izolr3T8kyqh1x32YkBH8sbRVLutCKEMwEXt2HEkoGmHC8wma2FUxzaUegrFY6aYf4wAXAw3eZ5cY1roBUQzAGZmYOJ_ENEh3_TcrNE-JepRH8QJWXlB0z_g8d0SLIDQr1Drc4TwsKtXDL1tliXTvb1t27ZE_eLPZ9V8z9pm1NAbg");
                   
            System.out.println(succMsg6);
            
            try {
                FileWriter fstream = new FileWriter("out.csv");
                out = new BufferedWriter(fstream);
            }
            catch (Exception e) {
                System.out.println(errMsg4);
            }
 
            /* Generem les parelles numtreads - temps mig */
            for (int n = 1; n <= maxThr; n += incr) {
                System.out.println("--------- NUEVA EJECUCION -----------");
                execucio(n);
                try {
                   sleep(1000);
                }
                catch (Exception e){
                   System.out.println(errMsg2);
                }
            }
            
            /* Tancar el fitxer */
            try {
                out.close();
            }
            catch (Exception e) {
                System.out.println(errMsg4);
            }
        }
        else {
            System.out.println(errMsg3);
        }
    }
    
    public static void execucio (int numThr) {
        /* Executem el test */
        Vector<Thread> thrs = new Vector();
        System.out.println(succMsg1);
        suma = 0;
        for (int i = 0; i < numThr; i++){
            Thread thr = new AppTest();
            thrs.add(thr);
            thr.start();
        }
        /* Espera a que tothom mori */
        boolean vivos = true;
        while (vivos) {
            int cont = 0;
            for (int i = 0; i < numThr; ++i) {
                if (!thrs.elementAt(i).isAlive()) {
                    cont = cont + 1;
                }
            } 
            vivos = (cont != numThr);
            try {
               sleep(1000);
            }
            catch (Exception e){
               System.out.println(errMsg2);
            }
        }
        
        /* Calcula la mitjana i la guarda a l'arxiu */
        float mitjana = ((float)suma)/((float)(numThr));
        //System.out.println();
        try{
            out.write(String.valueOf(numThr) + "," + String.valueOf(mitjana));
            out.newLine();
        }catch (Exception e){
            System.out.println(errMsg4);
        }
        
        /* Mostra resultats a l'usuari */
        System.out.println(succMsg2);
        System.out.println(endMsg1);
    }
    
    public void run() {
        Resultat res = enviarVot();
        System.out.println(succMsg3 + " " + getName());
        System.out.println(succMsg7 + " " + res.dni);
        System.out.println(succMsg4 + " " + res.message);
        System.out.println(succMsg5 + " " + String.valueOf(res.time) + " ms");
        suma = suma + res.time;
        try {
            sleep(1000);        
        }
        catch(Exception e){
            System.out.println(errMsg2);
        }
    }
    
    private Resultat enviarVot() {
        Resultat res = new Resultat();
        /* Calculem el DNI, el vot i la signatura */
        int posDni = (int) (dnis.size()*Math.random());
        int posVot = (int) (xvots.size()*Math.random());
        res.dni = dnis.elementAt(posDni);
        res.xvot = xvots.elementAt(posVot);
        res.signatura = signatures.elementAt(posVot);
        
        /* Enviem el vot */
        try {
            String params = "nif=" + URLEncoder.encode(res.dni, "utf-8")
                            + "&xvot=" + res.xvot
                            + "&signatura=" + res.signatura;
            long t1 = System.currentTimeMillis();
            res.message = enviaPOST(urlEmitVote, params);
            long t2 = System.currentTimeMillis();
            res.time = t2 - t1;
        } catch (Exception ex) {
            res.message = errMsg1;
            res.time = -1;
        }  
        return res;
    }

    
    private String enviaPOST(String url, String params) throws IOException {
        /* Prepare servlet connection */
        URL servletURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));

        /* Write POST parameters */
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(params);
        wr.flush();
        wr.close();

        /* Read servlet answer */
        DataInputStream urlin = new DataInputStream(conn.getInputStream());
        String resposta = "";
        String str;
        while ((str = urlin.readLine()) != null) {
            resposta = resposta + str;
        }
        urlin.close();
        
        return resposta;
    }
}
