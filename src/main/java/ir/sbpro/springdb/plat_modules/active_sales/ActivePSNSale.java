package ir.sbpro.springdb.plat_modules.active_sales;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ir.sbpro.models.StoreSale;
import ir.sbpro.springdb.plat_modules.sale_dlc_items.SaleDLCItem;
import ir.sbpro.springdb.plat_modules.sale_game_items.SaleGameItem;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "active_sales")
public class ActivePSNSale {
    @Id
    @Column(name = "id", nullable = false)
    private String ID;

    private String SaleTime;
    private String SaleEnd;
    private String NumGames;
    private String ImgURL;
    private String SaleName;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleGameItem> games;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleDLCItem> dlcList;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSaleTime() {
        return SaleTime;
    }

    public void setSaleTime(String saleTime) {
        SaleTime = saleTime;
    }

    public String getSaleEnd() {
        return SaleEnd;
    }

    public void setSaleEnd(String saleEnd) {
        SaleEnd = saleEnd;
    }

    public String getNumGames() {
        return NumGames;
    }

    public void setNumGames(String numGames) {
        NumGames = numGames;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String imgURL) {
        ImgURL = imgURL;
    }

    public String getSaleName() {
        return SaleName;
    }

    public void setSaleName(String saleName) {
        SaleName = saleName;
    }

    public List<SaleGameItem> getGames() {
        return games;
    }

    public void setGames(List<SaleGameItem> games) {
        this.games = games;
    }

    public List<SaleDLCItem> getDlcList() {
        return dlcList;
    }

    public void setDlcList(List<SaleDLCItem> dlcList) {
        this.dlcList = dlcList;
    }

    public void load(StoreSale saleFetcher){
        setID(saleFetcher.ID);
        setSaleTime(saleFetcher.SaleTime);
        setSaleEnd(saleFetcher.SaleEnd);
        setNumGames(saleFetcher.NumGames);
        setImgURL(saleFetcher.ImgURL);
        setSaleName(saleFetcher.SaleName);
    }
}
