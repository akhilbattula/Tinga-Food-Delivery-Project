<?php 
session_start();

include "../config.php";
mysqli_set_charset($conn, 'utf8mb4');

$res = array();

if(isset($_POST['username']) && isset($_POST['password'])){

    $username = mysqli_real_escape_string($conn,htmlspecialchars($_POST['username']));
    $password = mysqli_real_escape_string($conn,htmlspecialchars($_POST['password']));
    
    $query = "SELECT * FROM `tbl_restaurants` WHERE `username`='$username' AND `password`='$password'";

    $result = mysqli_query($conn, $query);
    
    if (!$result) {
        array_push($res,
          array('success'=>0,
                'message'=>"Server issue. Please agin later."
                    ));
                    
    }else{  
     
        $count = mysqli_num_rows($result);
        
        if($count === 0){
            array_push($res,
              array('success'=>0,
                    'message'=>"Invalid Credentials."
                    ));

        }else{
            
            $row = mysqli_fetch_array($result);
            $id = $row['restaurant_id'];
            
            $bean  = array('restaurant_id'=> $row['restaurant_id'],
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
                        
             array_push($res,array("success"=>1,
                    array("data" => $bean)
                    ));
            
        }
    }
}
echo json_encode(array('response' => $res));
