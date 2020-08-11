package com.dbms.boot.repository;

import com.dbms.boot.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface QueryRepository extends JpaRepository<Vehicle, String> {
    @Query(nativeQuery = true, value = "SELECT l.ENTRY_ID, l.YEAR, v.PRICE, v.COLOR, v.ODOMETER, v.TITLE_STATUS, v.CONDITION, v.REGION, v.STATE, c.MODEL_NAME, c.DESIGNED_BY\n" +
            "FROM LISTING l, jiaxu.VEHICLE v, CARMODEL c\n" +
            "WHERE v.VEHICLE_ID = l.CARID AND v.MODEL = c.MODEL_NAME\n" +
            "AND l.YEAR >= ?1 AND l.YEAR <= ?2 \n" +
            "AND c.DESIGNED_BY LIKE ?3 AND c.MODEL_NAME LIKE ?4 AND v.PRICE >= ?5 \n" +
            "AND v.PRICE <= ?6 AND v.ODOMETER >= ?7 AND v.ODOMETER <= ?8\n" +
            "AND v.STATE LIKE ?9 AND v.TITLE_STATUS LIKE ?10 AND v.COLOR LIKE ?11")
    List<Object[]> showSimple(Integer minYear, Integer maxYear, String manufacturer, String modelName, Integer minPrice, Integer maxPrice, Integer minOdo, Integer maxOdo, String state, String status, String color);

    @Query(nativeQuery = true, value = "SELECT DISTINCT t.avgprice, t2.numof, Listing.year\n" +
            "FROM LISTING,(SELECT AVG(jiaxu.VEHICLE.price) as avgprice, Listing.year\n" +
            "FROM LISTING, jiaxu.VEHICLE, CARMODEL\n" +
            "WHERE LISTING.CARID = jiaxu.VEHICLE.VEHICLE_ID AND jiaxu.VEHICLE.MODEL = CARMODEL.MODEL_NAME  AND CARMODEL.MODEL_NAME LIKE ?1 AND LISTING.year <= ?2 AND LISTING.year >= ?3 AND jiaxu.VEHICLE.CONDITION LIKE ?4 AND jiaxu.VEHICLE.title_status LIKE ?5 AND CARMODEL.fuel LIKE ?6\n" +
            "GROUP BY Listing.year) t, (SELECT COUNT(CARMODEL.transmission) as numof, Listing.year\n" +
            "FROM LISTING, jiaxu.VEHICLE, CARMODEL\n" +
            "WHERE LISTING.CARID = jiaxu.VEHICLE.VEHICLE_ID AND jiaxu.VEHICLE.MODEL = CARMODEL.MODEL_NAME  AND CARMODEL.MODEL_NAME LIKE ?1 AND LISTING.year <= ?2 AND LISTING.year >= ?3 AND jiaxu.VEHICLE.CONDITION LIKE ?4 AND jiaxu.VEHICLE.title_status LIKE ?5 AND CARMODEL.fuel LIKE ?6\n" +
            "GROUP BY Listing.year\n" +
            ") t2\n" +
            "WHERE  t.year = t2.year and t.year = Listing.year and Listing.year < 2020\n" +
            "ORDER BY LIsting.year DESC")
    List<Object[]> showComplex1(String modelName, Integer maxYear, Integer minYear, String condition, String status, String fuel);

    @Query(nativeQuery = true, value = "SELECT t.avgodo, t.year\n" +
            "FROM (SELECT AVG(jiaxu.VEHICLE.odometer) as avgodo, LISTING.year\n" +
            "FROM LISTING, jiaxu.VEHICLE, CARMODEL\n" +
            "WHERE LISTING.CARID = jiaxu.VEHICLE.VEHICLE_ID AND jiaxu.VEHICLE.MODEL = CARMODEL.MODEL_NAME AND CARMODEL.MODEL_NAME LIKE ?1 AND LISTING.year <= ?2 AND LISTING.year >= ?3 AND jiaxu.VEHICLE.CONDITION LIKE ?4 AND jiaxu.VEHICLE.title_status LIKE ?5 AND CARMODEL.fuel LIKE ?6\n" +
            "GROUP BY Listing.year\n" +
            ") t \n" +
            "WHERE t.year< 2020\n" +
            "ORDER BY t.year DESC")
    List<Object[]> showComplex2(String modelName, Integer maxYear, Integer minYear, String condition, String status, String fuel);

    @Query(nativeQuery = true, value = "SELECT DISTINCT t4.kinds,t4.numsize, Listing.year\n" +
            "FROM \n" +
            "Listing, (SELECT t2.kinds,t2.numsize, t2.year\n" +
            "FROM (SELECT COUNT(*) as numsize, CARMODEL.CAR_SIZE as kinds, Listing.year\n" +
            "FROM LISTING, jiaxu.VEHICLE, CARMODEL\n" +
            "WHERE LISTING.CARID = jiaxu.VEHICLE.VEHICLE_ID AND jiaxu.VEHICLE.MODEL = CARMODEL.MODEL_NAME AND CARMODEL.DESIGNED_BY LIKE ?1 AND CARMODEL.MODEL_NAME LIKE ?2 AND jiaxu.VEHICLE.CONDITION LIKE ?3 AND jiaxu.VEHICLE.title_status LIKE ?4 AND LISTING.year <= ?5 AND LISTING.year >= ?6\n" +
            "GROUP BY CARMODEL.CAR_SIZE, Listing.year\n" +
            ") t2\n" +
            "WHERE t2.numsize >= ALL(SELECT t3.numsize FROM (SELECT COUNT(*) as numsize, CARMODEL.CAR_SIZE as kinds, Listing.year\n" +
            "FROM LISTING, jiaxu.VEHICLE, CARMODEL\n" +
            "WHERE LISTING.CARID = jiaxu.VEHICLE.VEHICLE_ID AND jiaxu.VEHICLE.MODEL = CARMODEL.MODEL_NAME AND CARMODEL.DESIGNED_BY LIKE ?1 AND CARMODEL.MODEL_NAME LIKE ?2 AND jiaxu.VEHICLE.CONDITION LIKE ?3 AND jiaxu.VEHICLE.title_status LIKE ?4 AND LISTING.year <= ?5 AND LISTING.year >= ?6\n" +
            "GROUP BY CARMODEL.CAR_SIZE, Listing.year\n" +
            ") t3 WHERE t3.year = t2.year )) t4\n" +
            "WHERE  Listing.year = t4.year and Listing.year > 1900 and Listing.year < 2020\n" +
            "ORDER BY LIsting.year DESC")
    List<Object[]> showComplex3(String manufacturer, String modelName, String condition, String status, Integer maxYear, Integer minYear);

    @Query(nativeQuery = true, value = "SELECT DISTINCT t.stats, t.year\n" +
            "FROM LISTING,\n" +
            "(SELECT COUNT(jiaxu.VEHICLE.title_status) as stats, Listing.year\n" +
            "FROM LISTING, jiaxu.VEHICLE, CARMODEL\n" +
            "WHERE LISTING.CARID = jiaxu.VEHICLE.VEHICLE_ID AND jiaxu.VEHICLE.MODEL = CARMODEL.MODEL_NAME AND CARMODEL.DESIGNED_BY LIKE ?1 AND CARMODEL.MODEL_NAME LIKE ?2 AND jiaxu.VEHICLE.CONDITION LIKE ?3 AND jiaxu.VEHICLE.title_status LIKE ?4 AND LISTING.year <= ?5 AND LISTING.year >= ?6\n" +
            "GROUP BY Listing.year\n" +
            ") t\n" +
            "WHERE  t.year < 2020\n" +
            "ORDER BY t.year DESC")
    List<Object[]> showComplex4(String manufacturer, String modelName, String condition, String status, Integer maxYear, Integer minYear);

    @Query(nativeQuery = true, value = "SELECT DISTINCT t.num, t.year\n" +
            "FROM LISTING,(SELECT COUNT(*) as num, Listing.year\n" +
            "FROM LISTING, jiaxu.VEHICLE,CARMODEL\n" +
            "WHERE LISTING.CARID = jiaxu.VEHICLE.VEHICLE_ID AND jiaxu.VEHICLE.MODEL = CARMODEL.MODEL_NAME AND CARMODEL.DESIGNED_BY LIKE ?1  AND CARMODEL.MODEL_NAME LIKE ?2 AND LISTING.year <= ?3 AND LISTING.year >= ?4\n" +
            "GROUP BY Listing.year\n" +
            ") t\n" +
            "WHERE  t.year < 2020\n" +
            "ORDER BY t.year DESC")
    List<Object[]> showComplex5(String manufacturer, String modelName, Integer maxYear, Integer minYear);

    @Query(nativeQuery = true, value = "SELECT DISTINCT t.num, t.year\n" +
            "FROM LISTING,(SELECT COUNT(jiaxu.VEHICLE.VEHICLE_ID) as num, Listing.year\n" +
            "FROM LISTING, jiaxu.VEHICLE,CARMODEL\n" +
            "WHERE LISTING.CARID = jiaxu.VEHICLE.VEHICLE_ID AND jiaxu.VEHICLE.MODEL = CARMODEL.MODEL_NAME AND jiaxu.VEHICLE.STATE LIKE ?1 AND jiaxu.VEHICLE.region LIKE ?2 AND LISTING.year <= ?3 AND LISTING.year >= ?4\n" +
            "GROUP BY Listing.year\n" +
            ") t\n" +
            "WHERE  t.year < 2020\n" +
            "ORDER BY t.year DESC")
    List<Object[]> showComplex6(String state, String region, Integer maxYear, Integer minYear);

    @Query(nativeQuery = true, value = "select *\n" +
            "from \n" +
            "(\n" +
            "    (\n" +
            "    select R1.year, R1.brand as brand1, R1.sales as sales1, R2.brand as brand2, R2.sales as sales2, R3.brand as brand3, R3.sales as sales3\n" +
            "    from \n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2015\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R1,\n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2015\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R2,\n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2015\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R3\n" +
            "    where R1.year = R2.year and R2.year = R3.year and R1.rn = 1 and R2.rn = 2 and R3.rn = 3\n" +
            "    )\n" +
            "    UNION\n" +
            "    (\n" +
            "    select R1.year, R1.brand as brand1, R1.sales as sales1, R2.brand as brand2, R2.sales as sales2, R3.brand as brand3, R3.sales as sales3\n" +
            "    from \n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2016\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R1,\n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2016\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R2,\n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2016\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R3\n" +
            "    where R1.year = R2.year and R2.year = R3.year and R1.rn = 1 and R2.rn = 2 and R3.rn = 3\n" +
            "    )\n" +
            "    UNION\n" +
            "    (\n" +
            "    select R1.year, R1.brand as brand1, R1.sales as sales1, R2.brand as brand2, R2.sales as sales2, R3.brand as brand3, R3.sales as sales3\n" +
            "    from \n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2017\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R1,\n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2017\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R2,\n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2017\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R3\n" +
            "    where R1.year = R2.year and R2.year = R3.year and R1.rn = 1 and R2.rn = 2 and R3.rn = 3\n" +
            "    )\n" +
            "    UNION\n" +
            "    (\n" +
            "    select R1.year, R1.brand as brand1, R1.sales as sales1, R2.brand as brand2, R2.sales as sales2, R3.brand as brand3, R3.sales as sales3\n" +
            "    from \n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2018\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R1,\n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2018\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R2,\n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2018\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R3\n" +
            "    where R1.year = R2.year and R2.year = R3.year and R1.rn = 1 and R2.rn = 2 and R3.rn = 3\n" +
            "    )\n" +
            "    UNION\n" +
            "    (\n" +
            "    select R1.year, R1.brand as brand1, R1.sales as sales1, R2.brand as brand2, R2.sales as sales2, R3.brand as brand3, R3.sales as sales3\n" +
            "    from \n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2019\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R1,\n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2019\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R2,\n" +
            "        (\n" +
            "        select year, brand, sales, rownum as rn\n" +
            "        from (\n" +
            "            select L.year as year, M.designed_by as brand, count(*) as sales\n" +
            "            from Listing L, jiaxu.Vehicle V, CARMODEL M\n" +
            "            where L.carID = V.vehicle_id and M.model_name = V.model and year = 2019\n" +
            "            group by L.year, M.designed_by\n" +
            "            order by year, count(*) DESC\n" +
            "        )\n" +
            "        where rownum<=3\n" +
            "        ) R3\n" +
            "    where R1.year = R2.year and R2.year = R3.year and R1.rn = 1 and R2.rn = 2 and R3.rn = 3\n" +
            "    )\n" +
            ")\n" +
            "\n")
    List<Object[]> showComplex7();
}
