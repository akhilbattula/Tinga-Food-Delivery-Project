<?php
include("../config.php");

$lat = "16.786820";
$lon = "80.829455";

        $sql="SELECT *, ( 6371 * acos( cos( radians($lat) ) * cos( radians( `location_lat` ) ) * cos( radians( `location_long` ) - radians($lon) ) + sin( radians($lat) ) * sin( radians(`location_lat` ) ) ) ) AS distance FROM `tbl_restaurants` HAVING distance < 10 order by distance DESC LIMIT 1";


        if($result1 = mysqli_query($conn, $sql)){
        
              $row = mysqli_fetch_array($result1);
              echo 'name';
              echo $row['name'];
        }else{
            echo "Error adding user: " . mysqli_error($conn);
        
        }


?>