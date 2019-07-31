<?php 
session_start();

include "../config.php";

$response = array();
if(isset($_POST['rid'])){

  $rid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['rid']));

  $query = "SELECT * FROM `tbl_restaurants` WHERE `restaurant_id`='$rid'";
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
          $row = mysqli_fetch_array($result);
          $id = $row['restaurant_id'];

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
                array('data' => $bean)));

      }
  }
}
echo json_encode(array('response' => $response)); 