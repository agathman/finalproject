package com.FinalProject.demo.controllers;
import com.FinalProject.demo.Models.Country;
import com.FinalProject.demo.Models.CountryRepo;
import com.FinalProject.demo.Models.Users;
import com.FinalProject.demo.Models.UsersRepo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {

    DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    Date date = new Date();
    String currentDate = formatter.format(date);
    //Formatted to match API
    DateFormat formatter2 = new SimpleDateFormat("M/d/yy");




    @Autowired
    CountryRepo countryRepo;
    @Autowired
    UsersRepo usersRepo;

    Users currentUser = new Users();


    @RequestMapping(value = "/loginPage")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("loginPage");
        return mv;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
        public ModelAndView getLogin(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord) {

        Users userLogin = usersRepo.findByUserName(userName);
        String userPassword = userLogin.getPassword();

            if(passWord.equals(userPassword)) {
                ModelAndView mv = new ModelAndView("redirect:/");
                currentUser.setAdmin(userLogin.getAdmin());
                return mv;
            }
            else {
                String incorrect = "Username or Password is incorrect";
                ModelAndView mv = new ModelAndView("redirect:/");
                mv.addObject("incorrectLogin", incorrect);
                return mv;
            }
    }
    @RequestMapping(value = "/")
    public ModelAndView allCountries() {
        ModelAndView mv = new ModelAndView("index");
        String countries = getAllCountries();
        try {
            JSONArray json = new JSONArray(countries);
            Country[] allCountries = new Country[50];
            for(int i = 0; i < 50; i++) {
                JSONObject countryList = json.getJSONObject(i);
                String country = countryList.getString("country");
                allCountries[i] = new Country();
                allCountries[i].setCountryName(country);
                allCountries[i].setDate(currentDate);
                allCountries[i].setTotalCases((countryList.getInt("cases")));
                allCountries[i].setNewCases((countryList.getInt("todayCases")));
                allCountries[i].setTotalDeaths((countryList.getInt("deaths")));
                allCountries[i].setRecovered((countryList.getInt("recovered")));

            }
            mv.addObject("countryNames", allCountries);
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return mv;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
        public ModelAndView getCountry(@RequestParam ("country")String countryIso, @RequestParam ("date") String datePicked) throws ParseException {
        ModelAndView mv = new ModelAndView("viewCountry");
        String country = getCountryByISO(countryIso);

        DateFormat formatFor = new SimpleDateFormat("yyyy-MM-dd");
        Date foundDate = formatFor.parse(datePicked);
        String dateSelected = formatter2.format(foundDate);
        String dateForView = formatter.format(foundDate);

        Country selectedCountry = new Country();
        try {
            JSONObject json = new JSONObject(country);
            selectedCountry.setCountryName(json.get("country").toString());
            selectedCountry.setTotalCases(json.getJSONObject("timeline").getJSONObject("cases").getInt(dateSelected));
            selectedCountry.setDate(dateForView);
            selectedCountry.setTotalDeaths(json.getJSONObject("timeline").getJSONObject("deaths").getInt(dateSelected));
            selectedCountry.setRecovered(json.getJSONObject("timeline").getJSONObject("recovered").getInt(dateSelected));
            mv.addObject("country", selectedCountry);

        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return mv;

    }

    @RequestMapping(value = "/save/{countryName}", method = RequestMethod.POST)
    public ModelAndView save(@PathVariable("countryName") String name, @RequestParam ("date") String datePicked) throws ParseException {
                ModelAndView mv = new ModelAndView("redirect:/");

                DateFormat formatFor = new SimpleDateFormat("yyyy-MM-dd");
                Date htmlDate = formatFor.parse(datePicked);
                String date2 = formatFor.format(htmlDate);
                String dateSelected;

                if(datePicked.equals("noDate") || datePicked.equals(date2)) {
                    try {
                        String country = getCountry(name);
                        JSONObject json = new JSONObject(country);
                        Country countryToSave = new Country();
                        countryToSave.setId(UUID.randomUUID().toString());
                        countryToSave.setCountryName(json.get("country").toString());
                        countryToSave.setDate(currentDate);
                        countryToSave.setTotalCases(json.getInt("cases"));
                        countryToSave.setTotalDeaths(json.getInt("deaths"));
                        countryToSave.setRecovered(json.getInt("recovered"));
                        countryRepo.save(countryToSave);
                    } catch (Exception ex) {
                        System.out.println(ex.toString());

                    }
                }
                else{
                        Date foundDate = formatter2.parse(datePicked);
                        dateSelected = formatter2.format(foundDate);
                        String country = getCountryByISO(name);
                        String dateStored = formatter.format(foundDate);
                    try {
                        getCountryByISO(name);
                        JSONObject json = new JSONObject(country);
                        Country countryToSave = new Country();
                        countryToSave.setId(UUID.randomUUID().toString());
                        countryToSave.setCountryName(json.get("country").toString());
                        countryToSave.setDate(dateStored);
                        countryToSave.setTotalCases(json.getJSONObject("timeline").getJSONObject("cases").getInt(dateSelected));
                        countryToSave.setTotalDeaths(json.getJSONObject("timeline").getJSONObject("deaths").getInt(dateSelected));
                        countryToSave.setRecovered(json.getJSONObject("timeline").getJSONObject("recovered").getInt(dateSelected));
                        countryRepo.save(countryToSave);
                    }
                    catch (Exception ex) {
                        System.out.println(ex.toString());
                    }
                }

                return mv;
    }

    @RequestMapping(value = "/viewSnapshots")
    public ModelAndView viewSnapshots() {
        ModelAndView mv = new ModelAndView("viewSnapshots");
        mv.addObject("countryList", countryRepo.findAll());
        return mv;
    }

    @RequestMapping(value ="/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") String id) {
        ModelAndView mv;
        try {
        if (currentUser.getAdmin()) {
            mv = new ModelAndView("edit");
            Optional<Country> countryRecord = countryRepo.findById(id);
            Country country = countryRecord.get();
            mv.addObject("country", country);
            return mv;
        }

    }
    catch (NullPointerException ex) {
        mv = new ModelAndView("accessDenied");
        return mv;
        }
        ModelAndView mv2 = new ModelAndView("viewSnapshots");
    return mv2;
    }


    @RequestMapping(value="/submitChanges", method = RequestMethod.POST)
    public ModelAndView changes(@RequestParam("id")String id, @RequestParam("totalCases") String totalCases, @RequestParam("totalDeaths")String totalDeaths,
                                @RequestParam("newCases") String newCases) {
        ModelAndView mv = new ModelAndView("redirect:/viewSnapshots");
        Optional<Country> countryRecord = countryRepo.findById(id);
        Country country = countryRecord.get();

       // country.setTotalCases(Integer.parseInt(totalCases));
        country.setTotalDeaths(Integer.parseInt(totalDeaths));
        country.setNewCases(Integer.parseInt(newCases));
        countryRepo.save(country);
        return mv;
    }




    private String getCountryByISO(String ISO) {
        try {
            URL urlForGetRequest = new URL("https://disease.sh/v3/covid-19/historical/" + ISO + "?lastdays=356");
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            } else {
                return "Unexpected HTTP response";
            }
        }
            catch (Exception ex) {
                return "Exception: " + ex.getMessage();
            }
    }
    private String getCountry(String ISO) {
        try {
            URL urlForGetRequest = new URL("https://disease.sh/v3/covid-19/countries/"+ ISO + "?strict=true");
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            } else {
                return "Unexpected HTTP response";
            }
        }
        catch (Exception ex) {
            return "Exception: " + ex.getMessage();
        }
    }

    private String getAllCountries() {
        try {
            URL urlForGetRequest = new URL("https://disease.sh/v3/covid-19/countries?yesterday=true&sort=cases");
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            } else {
                return "Unexpected HTTP response";
            }
        }
        catch (Exception ex) {
            return "Exception: " + ex.getMessage();
        }

    }



}
