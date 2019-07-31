<?php 
session_start();

include "./config.php";

$response = array();

$query = "SELECT * FROM `tbl_restaurants` ORDER BY `status_tag` DESC, `rating` DESC";
$result = mysqli_query($conn, $query);
if (!$result) {
    echo mysqli_error($conn);
    array_push($response,
      array('success'=>0,
                'message'=>"Server issue. Please Try agin later.",
                ));
}else{                
    $count = mysqli_num_rows($result);
    if($count === 0){
        array_push($response,
          array('success'=>0,
                'message'=>"There are no restaurants in the database.",
                ));
    }else{
        while($row = mysqli_fetch_array($result))
        {
          $id = $row['restaurant_id'];
          //$query1 = "SELECT `image_path` FROM `tbl_images` WHERE `sid`='$id' LIMIT 1";
          //$result1 = mysqli_query($conn, $query1);
          //$row_image = mysqli_fetch_array($result1);
          $bean  = array('restaurant_id'=>$row['restaurant_id'],
                  'name'=>$row['name'],
                  'address'=>$row['address'],
                  'city'=>$row['city'],
                  'state'=>$row['state'],
                  'country'=>$row['country'],
                  'location_lat'=>$row['location_lat'],
                  'location_long'=>$row['location_long'],
                  'cuisine'=>$row['cuisine'],
                  'timings'=>$row['timings'],
                  'rating'=>$row['rating'],
                  'status'=>$row['status_tag'],
                  'image_path'=>$row['image_path']
                  );
                    
         array_push($response,
            array('success'=>1,
                array('data' => $bean)
                        ));


        }
    }
}

echo json_encode(array('response' => $response)); 