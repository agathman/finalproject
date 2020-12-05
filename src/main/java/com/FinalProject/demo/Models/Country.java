package com.FinalProject.demo.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "country")

public class Country {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "countryname")
    private String countryName;
    @Column(name = "date")
    private String date;
    @Column(name = "totalcases")
    private int totalCases;
    @Column(name = "totaldeaths")
    private int totalDeaths;
    @Column(name = "newcases")
    private int newCases;
    @Column(name = "userid")
    private String userId;
    @Column(name = "totalrecovered")
    private int recovered;

    public Country() {

    }


        public String getId () {
            return id;
        }

        public void setId (String id){
            this.id = id;
        }

        public String getCountryName () {
            return countryName;
        }

        public void setCountryName (String countryName){
            this.countryName = countryName;
        }

        public String getDate () {
            return date;
        }

        public void setDate (String date){
            this.date = date;
        }

        public int getTotalCases () {
            return totalCases;
        }

        public void setTotalCases ( int totalCases){
            this.totalCases = totalCases;
        }

        public int getTotalDeaths () {
            return totalDeaths;
        }

        public void setTotalDeaths ( int totalDeaths){
            this.totalDeaths = totalDeaths;
        }

        public int getNewCases () {
            return newCases;
        }

        public void setNewCases ( int newCases){
            this.newCases = newCases;
        }

        public String getUserId () {
            return userId;
        }

        public void setUserId (String userId){
            this.userId = userId;
        }

        public int getRecovered () {
            return recovered;
        }

        public void setRecovered ( int recovered){
            this.recovered = recovered;
        }

}
