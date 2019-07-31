<?php 
session_start();

include "../config.php";
mysqli_set_charset($conn, 'utf8mb4');

$response = array();
//$rid = "tinga_1";
//$keyword = "ALL";
$query ="";

if(isset($_POST['r_id']) && isset($_POST['keyword'])){
//if($rid!=null){

    $rid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['r_id']));
    $keyword = mysqli_real_escape_string($conn,htmlspecialchars($_POST['keyword']));
 
    $query = "SELECT * FROM `tbl_food_items` WHERE `sid`='$rid' AND `typetag`='$keyword'";

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
          
            while($row = mysqli_fetch_array($result))
            {
            
              $id = $row['food_id'];
              
              
              $bean  = array('food_id'=> $id,
                              'name'=>$row['name'],
                              'price'=>$row['price'],
                              'status'=>$row['status'],
                              'desc'=>$row['food_desc'],
                              'typetag'=>$row['typetag'],
                              'subtype_tag'=>$row['subtype_tag'],
                              'recommended'=>$row['recommended'],
                              'image_path'=>$row['image_path'] 
                              );
                        
             array_push($res,array("success"=>1,
                    array("data" => $bean)
                            ));
        
            }
          
        }
    }

}

echo json_encode(array('response' => $res)); 