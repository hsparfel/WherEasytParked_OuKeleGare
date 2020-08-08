package com.pouillos.viergeapp.entities;

import com.pouillos.viergeapp.dao.DaoSession;
import com.pouillos.viergeapp.dao.DepenseDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import com.pouillos.viergeapp.dao.BudgetDao;

@Entity
public class Budget {

    @Id
    private Long id;

    @NotNull
    private long depenseId;

    @ToOne(joinProperty = "depenseId")
    private Depense depense;

    @NotNull
    private Double montant;

    @NotNull
    private Date date;

    @NotNull
    private String dateString;

    @NotNull
    private int mois;

    @NotNull
    private int annee;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1883832454)
    private transient BudgetDao myDao;

    @Generated(hash = 1984229698)
    public Budget(Long id, long depenseId, @NotNull Double montant,
            @NotNull Date date, @NotNull String dateString, int mois, int annee) {
        this.id = id;
        this.depenseId = depenseId;
        this.montant = montant;
        this.date = date;
        this.dateString = dateString;
        this.mois = mois;
        this.annee = annee;
    }

    @Generated(hash = 1734026453)
    public Budget() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDepenseId() {
        return this.depenseId;
    }

    public void setDepenseId(long depenseId) {
        this.depenseId = depenseId;
    }

    public Double getMontant() {
        return this.montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString() {
        return this.dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public int getMois() {
        return this.mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public int getAnnee() {
        return this.annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    @Generated(hash = 1421387416)
    private transient Long depense__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 682674557)
    public Depense getDepense() {
        long __key = this.depenseId;
        if (depense__resolvedKey == null || !depense__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DepenseDao targetDao = daoSession.getDepenseDao();
            Depense depenseNew = targetDao.load(__key);
            synchronized (this) {
                depense = depenseNew;
                depense__resolvedKey = __key;
            }
        }
        return depense;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 151520704)
    public void setDepense(@NotNull Depense depense) {
        if (depense == null) {
            throw new DaoException(
                    "To-one property 'depenseId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.depense = depense;
            depenseId = depense.getId();
            depense__resolvedKey = depenseId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 571609092)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBudgetDao() : null;
    }

    

    

}
