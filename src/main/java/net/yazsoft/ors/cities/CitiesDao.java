package net.yazsoft.ors.cities;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Cities;
import net.yazsoft.ors.entities.Towns;
import net.yazsoft.ors.schools.SchoolsDao;
import net.yazsoft.ors.towns.TownsDao;
import net.yazsoft.ors.webclients.WebClientsDao;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class CitiesDao extends BaseGridDao<Cities> implements Serializable{
    Logger logger= Logger.getLogger(CitiesDao.class);

    Cities selected;

    List<Towns> towns;

    @Inject TownsDao townsDao;
    @Inject SchoolsDao schoolsDao;
    @Inject WebClientsDao webClientsDao;


    public CitiesDao() {
        super(Cities.class);
    }

    public void handleCityChange() {
        Cities city=schoolsDao.getItem().getRefCity();
        logger.info("CITY : " + city);
        if(city !=null && !city.equals(""))
            towns = townsDao.findByCity(city);
        else
            towns = new ArrayList<Towns>();
    }

    public void handleWebClientsCityChange() {
        Cities city=webClientsDao.getItem().getRefCity();
        logger.info("CITY : "+city);
        if(city !=null && !city.equals(""))
            towns = townsDao.findByCity(city);
        else
            towns = new ArrayList<Towns>();
    }

    public Cities getSelected() {
        return selected;
    }

    public void setSelected(Cities selected) {
        this.selected = selected;
    }

    public List<Towns> getTowns() {
        return towns;
    }

    public void setTowns(List<Towns> towns) {
        this.towns = towns;
    }
}
