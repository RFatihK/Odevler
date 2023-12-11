const fs = require("fs")

fs.readFile("ks_200_0", (err, data) => {
    if (err) throw err;

  let tumDosya = data.toString()
  let lines = tumDosya.split('\n')
  let weights = new Array()
  let values = new Array()
  let bag_size
  let n
  let usepath = new Array() // 0-1 değerlini tutan dizi
  let usedvalues = new Array() // hangi valuelerin alındığını tutan dizi
  


 
  for (let i = 0; i < lines.length-1; i++) {
    
    
    let line = lines[i]
    let nums = line.split(" ")

    i==0 ? 
    bag_size=parseInt(nums[1])
     :
    values.push(parseInt(nums[0])) &&
    weights.push(parseInt(nums[1]))
  }

  n = values.length


  
  console.log("Bag size : " +bag_size +" \nWeights : " + weights +"\nValues : "  + values+ "\nFile Size : " + n)



// Dinamik Programlama tabanlı bir çözüm
    // en fazla iki tamsayı döndüren 0-1 sırt çantası fonksiyonu
    
    function max(a, b)
    {
          return (a > b) ? a : b;
    }
  
    // W kapasiteli bir sırt çantasına konulabilecek maksimum değeri veren kısım
    function knapSack(W, wt, val, n)
    {
        let i, w;
        let K = new Array(n + 1);
  
        // Build table K[][] in bottom up manner
        for (i = 0; i <= n; i++)
        {
            K[i] = new Array(W + 1);
            for (w = 0; w <= W; w++)
            {
                if (i == 0 || w == 0)
                    K[i][w] = 0;
                else if (wt[i - 1] > w)
                K[i][w] = K[i - 1][w];
                    
                else{
                K[i][w]
                        = max(val[i - 1]
                         + K[i - 1][w - wt[i - 1]],
                         K[i - 1][w]);
                        }
                    
            }
        }

        let result = K[n][W] 

        w = W;
        for (i = n; i > 0 && result > 0; i--)
        {
   
            
            if (result == K[i - 1][w])
                continue;
            else {
   
                // Kullanılan Değer
                usepath[i-1] = 1
                //console.log(wt[i - 1] + " ");
                usedvalues.push(val[i-1])
   
                // Bu ağırlk dahil edildiğinden düşürlür
                result = result - val[i - 1];
                w = w - wt[i - 1];
            }
        }
  
        return K[n][W];
    }


    


        for (let index = 0; index < n; index++) {
            if(usepath[index]==null)
            usepath[index] = 0 
        }



     
    console.log(knapSack(bag_size, weights, values, n));
    console.log(usedvalues)
    console.log(usepath)

})