package net.yazsoft.ors.optic;

import net.yazsoft.ors.entities.Optics;
import net.yazsoft.ors.entities.OpticsParts;

public class OpticsPartsDto extends OpticsParts{
    Optics optic;

    public OpticsPartsDto() {
        super();
    }

    public OpticsPartsDto(OpticsParts part) {
        this.setTid(part.getTid());
        this.setActive(part.getActive());
        this.setVersion(part.getVersion());
        this.setCreated(part.getCreated());
        this.setUpdated(part.getUpdated());
        this.setRefOptic(part.getRefOptic());
        this.setChars(part.getChars());
        this.setCharType(part.getCharType());
        this.setHorizontal(part.isHorizontal());
        this.setPrintTitle(part.isPrintTitle());
        this.setRank(part.getRank());
        this.setX(part.getX());
        this.setY(part.getY());
        this.setW(part.getW());
        this.setH(part.getH());
        this.setPrint(part.isPrint());
        this.setRefFieldType(part.getRefFieldType());
        this.setTitle(part.getTitle());
    }


    public OpticsParts toEntity(){
        return toEntity(null);
    }
    public OpticsParts toEntity(OpticsParts entity){
        if (entity==null) {
            entity=new OpticsParts();
        }
        entity.setTid(this.getTid());
        entity.setActive(this.getActive());
        entity.setVersion(this.getVersion());
        entity.setCreated(this.getCreated());
        entity.setUpdated(this.getUpdated());
        entity.setRefOptic(this.getRefOptic());
        entity.setChars(this.getChars());
        entity.setCharType(this.getCharType());
        entity.setHorizontal(this.isHorizontal());
        entity.setPrintTitle(this.isPrintTitle());
        entity.setRank(this.getRank());
        entity.setTitle(this.getTitle());
        entity.setX(this.getX());
        entity.setY(this.getY());
        entity.setW(this.getW());
        entity.setH(this.getH());
        entity.setPrint(this.isPrint());
        entity.setRefFieldType(this.getRefFieldType());
        return entity;
    }

    public int findX() {
        return optic.getMarginx()+(getX()*optic.getRatio());
    }

    public int findY() {
        return optic.getMarginy()+(getY()*optic.getRatio());
    }

    public int findW() {
        return ((getW()+1)-getX())*optic.getRatio();
    }

    public int findH() {
        return ((getH()+1)-getY())*optic.getRatio();
    }

    public Optics getOptic() {
        return optic;
    }

    public void setOptic(Optics optic) {
        this.optic = optic;
    }

    @Override
    public String toString() {
        return "OpticsPartsDto{" +
                "optic=" + optic +
                "}  " + super.toString();
    }
}
