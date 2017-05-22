package com.qiandu.live.http.response;


import com.qiandu.live.http.IDontObfuscate;

/**
 * Created by admin on 2017/4/21.
 */
public class FanilyDetailResp extends IDontObfuscate {
    private int  clanId;
    private String clanLogo;
    private String   clanName;
    private String clanDesc;
    private String leadId;
    private int isLeader;
    private int count;
    private String clanLead;

    public int getClanId() {
        return clanId;
    }

    public void setClanId(int clanId) {
        this.clanId = clanId;
    }

    public String getClanLogo() {
        return clanLogo;
    }

    public void setClanLogo(String clanLogo) {
        this.clanLogo = clanLogo;
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public String getClanDesc() {
        return clanDesc;
    }

    public void setClanDesc(String clanDesc) {
        this.clanDesc = clanDesc;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public int getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(int isLeader) {
        this.isLeader = isLeader;
    }

    public String getClanLead() {
        return clanLead;
    }

    public void setClanLead(String clanLead) {
        this.clanLead = clanLead;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
