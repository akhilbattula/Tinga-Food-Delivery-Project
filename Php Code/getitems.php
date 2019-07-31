<?php 
session_start();

include "./config.php";

$response = array();
$rid = "tinga_1";
$keyword = "ALL";
$query ="";
//print "0";

//if(isset($_POST['r_id']) && isset($_POST['keyword'])){
if($rid!=null){
//print "1";
    //$rid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['r_id']));
    //$keyword = mysqli_real_escape_string($conn,htmlspecialchars($_POST['keyword']));
 
   
    $query = "SELECT * FROM `tbl_food_items` WHERE `sid`='$rid'";
   
    $result = mysqli_query($conn, $query);
    $res = array();
    
    if (!$result) {
    
        array_push($res,
          array('success'=>0,
                    'message'=>"Server issue. Please agin later.",
                    ));
   
    }else{  
     
        $count = mysqli_num_rows($result);
        
        if($count === 0){
            array_push($res,
              array('success'=>0,
                    'message'=>"There are no food items this restaurant in the database.",
                    ));
        }else{
          
            array_push($res,"success"=>1);
          
            while($row = mysqli_fetch_array($result))
            {
              
              $bean  = array('food_id'=> $row['food_id'],
                              'name'=>$row['name'],
                              'price'=>$row['price'],
                              'desc'=>$row['food_desc'],
                              'typetag'=>$row['typetag'],
                              'recommended'=>$row['recommended'],
                              'image_path'=>$row['image_path'] 
                              );
                        
              array_push($res, array("data" => $bean));
            }
            echo json_encode(array("response" => $res));        
        }
    }
}
//echo json_encode(array('response' => $res)); 